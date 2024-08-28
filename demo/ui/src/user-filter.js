import {LitElement, html} from "lit";

class UserFilter extends LitElement {
    static get properties() {
        return {

        }
    }

    constructor() {
        super();
        this.name = "";
        this.email = "";
        this.streetname = "";
        this.housenumber = "";
        this.city = "";
        this.country = "";
    }

    createWhereClause() {
        return ['name', 'email', 'streetname', 'housenumber', 'city', 'country']
            .filter(item => this[item] && this[item] !== '')
            .map(item => `${item} like '%${this[item]}%'`)
            .join(" AND ");
    }

    change(e) {
        this[e.target.id] = e.target.value;
        this.dispatchEvent(new CustomEvent('filterupdate-event', {
            detail: {message: this.createWhereClause()},
            bubbles: true,
            composed: true
        }));
    }

    render() {
        return html`
            <style>
                :host {
                    width: 100%;
                }

                .header {
                    height: 1em;
                    display: inline-block;
                    width: 16%;
                }
            </style>
           `;
    }

}

if (!customElements.get('x-user-filter')) {
    customElements.define('x-user-filter', UserFilter);
}