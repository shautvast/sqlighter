import {LitElement, html} from 'lit';
import './item.js';
import './virtual-scroll';
import './styles.css';

const columns = ['name', 'email', 'streetname', 'housenumber', 'city', 'country'];

class App extends LitElement {

    static get properties() {
        return {
            data: {type: Array},
            dbWorker: {},
            filters: {type: Object},
            where: {},
            sort: {type: Object}
        }
    }

    constructor() {
        super();
        this.filters = {
            name: {visible: "hidden", value: ""},
            email: {visible: "hidden", value: ""},
            streetname: {visible: "hidden", value: ""},
            housenumber: {visible: "hidden", value: ""},
            city: {visible: "hidden", value: ""},
            country: {visible: "hidden", value: ""},
        }
        this.where = "";
        this.data = [];
        this.sort = {
            prevColumn: undefined,
            column: undefined,
            order: undefined
        }

        // sqlite needs a worker thread
        // there is also a promise based sqlite wrapper, but the esm wrapper used here does not support that
        this.dbWorker = new Worker(new URL('./database.js', import.meta.url), {
            type: 'module',
        });

        // listen for events from the database worker
        this.dbWorker.onmessage = (e) => {
            if (e.data === "db ready") {
                this.dbWorker.postMessage("SELECT rowid, name, email, streetname, housenumber, city, country FROM customers");
            } else {
                if (e.data) {
                    this.data = e.data;
                }
            }
        }

        // listen for user input, execute the updated query
        this.addEventListener('filterupdate-event', e => {
            this.where = e.detail.message;
            this.updateQuery();
        });

        document.getElementById("body").addEventListener("keyup", e => {
            if (e.code === 'Escape') {
                columns.forEach(c => this.filters[c].visible = "hidden");
            }
            this.requestUpdate();
        });
    }

    updateQuery() {
        let query = "SELECT rowid, name, email, streetname, housenumber, city, country FROM customers";
        if (this.where.length > 0) {
            query += ` WHERE ${this.where}`;
        }
        if (this.sort.column && this.sort.order) {
            query += ` ORDER BY ${this.sort.column} ${this.sort.order}`
        }
        this.dbWorker.postMessage(query);
    }

    firstUpdated() {
        columns.forEach(c => {
            document.getElementById(c).addEventListener("click", e => {
                this.filters[e.target.id].visible = "visible";
                this.requestUpdate();
                setTimeout(() => this.shadowRoot.getElementById(`i-${e.target.id}`).focus(), 10);
            });

            document.getElementById(`s-${c}`).addEventListener("click", e => {
                this.sort.prevColumn = this.sort.column;
                this.sort.column = e.target.id.substring(2);
                if (this.sort.prevColumn !== this.sort.column){
                    this.sort.order = undefined;
                }

                if (!this.sort.order) {
                    this.sort.order = "ASC";
                } else if (this.sort.order === 'ASC') {
                    this.sort.order = "DESC";
                } else {
                    this.sort.order = undefined;
                }
                this.updateQuery();
            });

            const element = this.shadowRoot.getElementById(`f-${c}`);
            element.addEventListener("keyup", e => {
                    if (e.code === 'Escape' || e.code === 'Enter') {
                        this.filters[e.target.id.substring(2)].visible = "hidden";
                        this.requestUpdate();
                    }
                }
            );

            element.addEventListener("focusout", e => {
                    this.filters[e.target.id.substring(2)].visible = "hidden";
                    this.requestUpdate();
                }
            );
        });
    }

    render() {
        return html`
            <style>
                :host .filter {
                    z-index: 1;
                    border: solid #888 2px;
                    border-radius: 15px;
                    background: white;
                    position: absolute;
                    top: 2%;
                    width: 10em;
                }

                input {
                    margin: 5px;
                    z-index: 2;
                    border: none;
                    width: auto;
                }

                input:focus {
                    outline: none !important;

                }
            </style>

            <div>
                <div class="filter" id="f-name"
                     style="left: 2%;visibility: ${this.filters.name.visible}">
                    <input id="i-name" placeholder="name" .value=${this.filters.name.value}
                           @input=${(e) => this.change(e)}>
                </div>
                <div class="filter" id="f-email" style="left: 20%;visibility: ${this.filters.email.visible}">
                    <input id="i-email" placeholder="email" .value=${this.filters.email.value}
                           @input=${(e) => this.change(e)}>
                </div>
                <div id="f-streetname" class="filter" style="left: 34%;visibility: ${this.filters.streetname.visible}">
                    <input id="i-streetname" placeholder="streetname" .value=${this.filters.streetname.value}
                           @input=${e => this.change(e)}>
                </div>
                <div id="f-housenumber" class="filter"
                     style="left: 50%;visibility: ${this.filters.housenumber.visible}">
                    <input id="i-housenumber" placeholder="housenumber" .value=${this.filters.housenumber.value}
                           @input=${(e) => this.change(e)}>
                </div>
                <div id="f-city" class="filter" style="left: 66%;visibility: ${this.filters.city.visible}">
                    <input id="i-city" placeholder="city" .value=${this.filters.city.value}
                           @input=${(e) => this.change(e)}>
                </div>
                <div id="f-country" class="filter" style="left: 82%;visibility: ${this.filters.country.visible}">
                    <input id="i-country" placeholder="country" .value=${this.filters.country.value}
                           @input=${(e) => this.change(e)}>
                </div>
            </div>

            <x-virtual-scroller
                    size="${18}"
                    .items="${this.data}"
                    .idFn="${a => a[0]}"
                    .renderFn="${a => html`
                        <x-item .item="${a}"/>`}">
            </x-virtual-scroller>
            <div style="position: absolute;bottom: 10%;left: 10px">${this.data.length} results</div>`;
    }

    createWhereClause() {
        return columns
            .filter(item => this.filters[item].value && this.filters[item].value !== '')
            .map(item => `${item} like '%${this.filters[item].value}%'`)
            .join(" AND ");
    }

    change(e) {
        this.filters[e.target.id.substring(2)].value = e.target.value;
        this.dispatchEvent(new CustomEvent('filterupdate-event', {
            detail: {message: this.createWhereClause()},
            bubbles: true,
            composed: true
        }));
    }
}

if (!customElements.get('virtual-scroller')) {
    customElements.define('virtual-scroller', App);
}