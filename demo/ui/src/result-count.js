import {LitElement, html} from "lit";

class ResultCount extends LitElement {
    static get properties() {
        return {
            count: {type: Number, attribute: false},
        }
    }

    constructor() {
        super();
        this.count = 10;
        this.addEventListener('someEvent', e => {
            console.log("count");
            this.count = e.detail.message;
        });
    }

    render() {
        return html`
            <p>${this.count} results</p>
        `;
    }
}

if (!customElements.get('x-result-count')) {
    customElements.define('x-result-count', ResultCount);
}