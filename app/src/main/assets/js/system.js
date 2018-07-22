        function appendSuccessLog(data){
                document.getElementById("logsuccess").innerHTML += data;
        }
        function appendErrorLog(data){
                document.getElementById("logerror").innerHTML += data;
        }
        function appendOtherLog(data){
                document.getElementById("logother").innerHTML += data;
        }
        function vibrate() {
            var delay = document.getElementById("system_delay").value;
            var system = new System();
            system.vibrate(
                delay,
                function(success){
                    appendSuccessLog(success);
                },
                function(error){
                    appendErrorLog(error);
                }
            );
        }

        function light() {
            var system = new System();
            system.light(
                function(success){
                    appendSuccessLog(success);
                },
                function(error){
                    appendErrorLog(error);
                }
            );
        }
