'use strict';

app.factory('TokenStorage', function() {
    var storageKey = 'auth_token';

    return {
        store: function(token) {
            localStorage.setItem(storageKey, token);
        },

        retrieve: function() {
            return localStorage.getItem(storageKey);
        },

        clear: function() {
            localStorage.removeItem(storageKey)
        }
    }
});
