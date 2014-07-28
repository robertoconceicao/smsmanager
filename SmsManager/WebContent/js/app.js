/**
 * AngularJS module to process a form.
 */
angular.module('myApp', ['ajoslin.promise-tracker'])
  .controller('help', function ($scope, $http, $log, promiseTracker, $timeout) {
    // Form submit handler.
    $scope.submit = function(form) {
      // Trigger validation flag.
      $scope.submitted = true;

      // If form is invalid, return and let AngularJS show validation errors.
      if (form.$invalid) {
        return;
      }

      // Default values for the request.
      $scope.progress = promiseTracker('progress');
      
      var config = {
        params : {
          'de' : $scope.de,
          'phone' : $scope.phone,
          'sms' : $scope.sms          
        },
        tracker : 'progress'
      };

      // Perform JSONP request.
//      $http.jsonp('response.json', config)
//      $http.get('SmsManager', config)
//      $http.get('http://192.168.0.4:8080/SmsManager/SmsManager', config)
//      $http({ method: "post",
//    	  url: "http://192.168.0.4:8080/SmsManager/SmsManager",
//    	  params: {    		 
//    		  de : $scope.de,
//              phone : $scope.phone,
//              sms : $scope.sms
//    	  },
//    	  data: {
//    		  teste: "teste de data"
//    	  }
//      })
      $http.get('http://192.168.0.4:8080/SmsManager/SmsManager', config)
      .success(function(data, status, headers, config) {
          if (data.status == 'OK') {
            $scope.de = null;
            $scope.phone = null;
            $scope.sms = null;
            $scope.messages = 'Sms enviado com sucesso!';
            $scope.submitted = false;
            $scope.error = null;
          } else {
        	$scope.messages = null;
            $scope.error = 'Erro ao tentar enviar o sms, favor tente mais tarde.';
            $log.error(data);
          }
        })
        .error(function(data, status, headers, config) {
          $scope.messages = null;
          $scope.progress = data;
          $scope.error = 'Problemas de rede. Tente mais tarde';
          $log.error(data);
        });

      // Hide the status message which was set above after n seconds.
      $timeout(function() {
        $scope.messages = null;
        $scope.error = null;
      }, 5000);
    };
  });