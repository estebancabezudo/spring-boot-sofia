class FacebookSocialButton extends SocialButton {
    constructor(options) {
        super({
            element: options.element,
            url: '/oauth2/authorization/facebook',
            image: '/images/login/socialButtons/facebook/1.00/logoFacebook.svg',
            textKey: 'facebookButtonText'
        });
    }
}