class EMailInput extends Input {
    constructor(options) {
        super({
            element: options.element,
            validator: value => {
                const regex = /^[A-Za-z0-9!#$%&'*+/=?^_`@{|}~\.-]+$/;
                return value.match(regex);
            },
            url: options.url,
            invalidCharacterMessageKey: 'emailInvalidCharacterOnAddress'
        });
    };
}
