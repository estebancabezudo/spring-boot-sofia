class GoogleSocialButton extends SocialButton {
    constructor(options) {
        super({
            element: options.element,
            url: '/oauth2/authorization/google',
            image: '/images/login/socialButtons/google/1.00/logoGoogle.png',
            textKey: 'googleButtonText'
        });
    }
}