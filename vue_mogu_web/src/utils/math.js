export default {
    MathConfig() {
        this.loadjs('../../../static/js/jquery-1.9.1.min.js');
        MathJax.Hub.Config({
            showProcessingMessages: false,
            messageStyle: "none",
            extensions: ["tex2jax.js"],
            jax: ["input/TeX", "output/HTML-CSS"],
            tex2jax: {
                inlineMath: [["$", "$"]],
                displayMath: [["", "", ""]],
                skipTags: ['script', 'noscript', 'style', 'textarea', 'pre', 'code', 'a'],
                ignoreClass: "comment-content"
            },
            "HTML-CSS": {
                availableFonts: ["STIX", "TeX"],
                showMathMenu: true
            }
        });
    }
}