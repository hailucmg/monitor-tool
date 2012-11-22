$(document).ready(function () {
    console.log('document ready');
    app = new AppRouter();
    Backbone.history.start();
});