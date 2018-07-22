        function appendSuccessLog(data){
                document.getElementById("logsuccess").innerHTML += data+"<br>";
        }
        function appendErrorLog(data){
                document.getElementById("logerror").innerHTML += data+"<br>";
        }
        function appendOtherLog(data){
                document.getElementById("logother").innerHTML += data+"<br>";
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

        function deviceinfo() {
            var system = new System();
            system.deviceinfo(
                function(success){
                    //json string
//                    console.log(success)
                    appendSuccessLog(json2str(success));
//                    appendSuccessLog(JSON.parse(success));
//                    appendSuccessLog(success);
                },
                function(error){
                    appendErrorLog(error);
                }
            );
        }
        function json2str(o) {
			var arr = [];
			var fmt = function(s) {
			if (typeof s == 'object' && s != null) return json2str(s);
                return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s;
			}
			for (var i in o)
                 arr.push("'" + i + "':" + fmt(o[i]));
            return  arr.join('<br>');
        }
