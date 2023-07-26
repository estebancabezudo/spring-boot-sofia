class BurgerMenu {
    constructor(configuration) {
        this.beforeShow = configuration.beforeShow;
        this.beforeHide = configuration.beforeHide;
        const validateElements = (menuContainerId, openMenuButtonId, closeMenuButtonId) => {
            this.menuContainer = document.getElementById(menuContainerId);
            if (!this.menuContainer) {
                throw new Error(`Can't find the element with the id ${menuContainerId}`);
            }
            this.openMenuButton = document.getElementById(openMenuButtonId);
            if (!this.openMenuButton) {
                throw new Error(`Can't find the element with the id ${openMenuButtonId}`);
            }
            this.closeMenuButton = document.getElementById(closeMenuButtonId);
            if (!this.closeMenuButton) {
                throw new Error(`Can't find the element with the id ${closeMenuButtonId}`);
            }
        }
        const createGUI = (items) => {
            this.subMenus = new Array();
            this.menuVisible = false;

            this.openMenuButton.addEventListener('click', event => {
                if (this.menuVisible) {
                    this.hide();
                    event.stopPropagation();

                } else {
                    this.show();
                    event.stopPropagation();
                }
            });
            if (openMenuButtonId != closeMenuButtonId) {
                this.closeMenuButton.addEventListener('click', event => {
                    this.hide();
                    event.stopPropagation();
                });
            }
            document.addEventListener('click', event => {
                this.hide();
                event.stopPropagation();
            });
            const showItem = item => {
                const element = document.getElementById(item.id);
                element.addEventListener('click', event => {
                    console.log(`BurguerMenu::constructor::createGUI::showItem: Click on ${event.target.id}`);
                    if (Core.isString(item.onclick)) {
                        location.href = item.onclick;
                    }
                })
                element.classList.remove('burgerMenuHiddenMenuItem');
            }
            const hideItem = item => {
                const element = document.getElementById(item.id);
                element.classList.add('burgerMenuHiddenMenuItem');
            }
            items.forEach(item => {
                if (item.hide || item.show) {
                    hideItem(item);
                }
            });

            Core.subscribeTo('core:webClientDataChange', () => {
                items.forEach(item => {

                    if (item.hide && Core.isValid(item.hide)) {
                        hideItem(item);
                        return;
                    }

                    if (item.show && Core.isValid(item.show)) {
                        showItem(item);
                        return;
                    }

                    showItem(item);

                });
            });
        };
        const menuContainerId = configuration.menuContainerId;
        const openMenuButtonId = configuration.openMenuElementId;
        const closeMenuButtonId = configuration.closeMenuElementId;
        const items = configuration.items;

        validateElements(menuContainerId, openMenuButtonId, closeMenuButtonId);
        createGUI(items);
    };
    show() {
        if (Core.isFunction(this.beforeShow)) {
            this.beforeShow();
        }
        if (!this.menuVisible) {
            this.menuVisible = true;
            this.menuContainer.classList.add('burguerMenuContainerActive');
        }
    };
    hide() {
        if (Core.isFunction(this.beforeHide)) {
            this.beforeHide();
        }
        if (this.menuVisible) {
            this.menuVisible = false;
            this.menuContainer.classList.remove('burguerMenuContainerActive');
        }
    };
}
