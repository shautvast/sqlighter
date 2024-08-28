import {LitElement, html} from 'lit';

class Item extends LitElement {
    render() {
        return html`
            <style>
                :host {
                    width: 100%;
                }

                :host .column {
                    display: inline-block;
                    width: 16%;
                    overflow: hidden
                }
                
                .row{
                    border: solid #fff 1px;
                }
            </style>
            <div class="row">
                <div class="column">${this.item[1]}</div>
                <div class="column">${this.item[2]}</div>
                <div class="column">${this.item[3]}</div>
                <div class="column">${this.item[4]}</div>
                <div class="column">${this.item[5]}</div>
                <div class="column">${this.item[6]}</div>
            </div>
        `;
    }
}

Item.properties = () => ({
    item: {type: Object, attribute: false, reflect: false},
});

if (!customElements.get('x-item')) {
    customElements.define('x-item', Item);
}
