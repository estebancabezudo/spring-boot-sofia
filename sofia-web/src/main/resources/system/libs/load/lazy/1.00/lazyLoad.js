class LazyLoad {
    constructor(options) {
        const getElement = target => {
            if (target === undefined || target == null) {
                throw new Error('You MUST specify an id or element.');
            }
            if (Core.isString(target)) {
                const id = target;
                console.log(`Using the id ${id}`);
                const element = document.getElementById(id);
                if (element === null || element === undefined) {
                    throw new Error(`Element NOT FOUND: ${target}`);
                }
                return element;
            }
            if (Core.isHTMLDivElement(target)) {
                console.log(`Using the element `, target);
                this.element = target;
                return element;
            }
        }

        const getLimit = () => {
            const rect = this.element.getBoundingClientRect();
            return (rect.top + (rect.bottom - rect.top) / 2);
        }

        const checkScroll = () => {
            if (getLimit() <= window.innerHeight && !this.element.classList.contains("loaded")) {
                setTimeout(() => { this.element.classList.add("loaded"); }, 600);
            }
        }

        this.element = getElement(options.target);

        window.addEventListener("scroll", checkScroll);
        checkScroll();

    }
}