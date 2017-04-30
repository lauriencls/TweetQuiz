var app = angular.module('tweetQuiz');

app.run(['GAuth', 'GApi', '$rootScope',
    function(GAuth, GApi, $rootScope) { 
		
		//Declarations
		$rootScope.BASE = 'https://1-dot-tweetquiz-161808.appspot.com/_ah/api';
		
		//Loading Gapi
        GApi.load('tweetquizendpoint', 'v1', $rootScope.BASE).then(function(resp) {
            console.log('api: ' + resp.api + ', version: ' + resp.version + ' loaded');
            console.log("GAPI loaded");
        }, function(resp) {
            console.log('an error occured during loading api: ' + resp.api + ', resp.version: ' + version);
        });
}]);
