import {LitElement, html} from 'lit';
import {repeat} from 'lit/directives/repeat.js';
import './user-filter.js';

class VirtualScroller extends LitElement {
    constructor() {
        super();
        this.currentBase = this.currentBase === undefined ? 0 : this.currentBase;
        this.onScroll = this.onScroll.bind(this);
        this.size = this.size || 18;
    }

    connectedCallback() {
        super.connectedCallback();
        this.addEventListener('scroll', this.onScroll);
        this.wrapperHeight = this.getClientRects()[0].height;
    }

    onScroll(event) {
        if (this.scrollTicking) {
            window.cancelAnimationFrame(this.scrollTicking);
            this.scrollTicking = null;
        }
        this.scrollTicking = window.requestAnimationFrame(() => {
            this.currentBase = Math.round(this.scrollTop / this.size);
        });
    }

    register(inputElements){
        inputElements.addEventListener("change");
    }

    render() {
        const listSize = Math.round(this.wrapperHeight / this.size) + 2;
        const filteredList = this.items.slice(
            this.currentBase,
            this.currentBase + listSize
        );
        this.style.setProperty(
            '--vs-top-height',
            `${this.currentBase * this.size}px`
        );
        this.style.setProperty(
            '--vs-bottom-heig ht',
            `${(this.items.length - listSize - this.currentBase) * this.size}px`
        );
        return html`
            <style>
                :host {
                    border: solid #eee 1px;
                    margin-top: 5px;
                    padding: 3px;
                    position: relative;
                    overflow: scroll;
                    width: 100%;
                }

                :host .top-padding {
                    height: var(--vs-top-height);
                }

                :host .bottom-padding {
                    height: var(--vs-bottom-height);
                }
            </style>
            <div class="top-padding"></div>
            
            ${repeat(filteredList, this.idFn, this.renderFn)}
            <div class="bottom-padding"></div>
        `;
    }
}

Object.defineProperty(VirtualScroller, 'properties', {
    get: () => ({
        size: {type: Number, attribute: true, reflect: true},
        currentBase: {type: Number, attribute: true, reflect: true},
        items: {type: Array, attribute: false, reflect: false},
        idFn: {type: Function, attribute: false, reflect: false},
        renderFn: {type: Function, attribute: false, reflect: false},
    }),
});

if (!customElements.get('x-virtual-scroller')) {
    customElements.define('x-virtual-scroller', VirtualScroller);
}
