				var lien="";
				$('.show').on('click', function (e) {
					lien=$(this).next(".lien").attr("href");
				});
				var _CANVAS = document.querySelector("#video-canvas"),
				_CTX = _CANVAS.getContext("2d"),
				_VIDEO = document.querySelector("#main-video");	
				
				$('#exampleModal').on('show.bs.modal', function (e) {

					// Object Url as the video source
					document.querySelector("#main-video source").setAttribute('src', lien);
					// Load the video and show it
					_VIDEO.load();
					_VIDEO.style.display = 'inline';
					
					// Load metadata of the video to get video duration and dimensions
					_VIDEO.addEventListener('loadedmetadata', function() { console.log(_VIDEO.duration);
					    var video_duration = _VIDEO.duration,
					    	duration_options_html = '';
					    // Set canvas dimensions same as video dimensions
					    _CANVAS.width = _VIDEO.videoWidth;
						_CANVAS.height = _VIDEO.videoHeight;
					});
		    
				});
				$(document).on('hide.bs.modal', "#exampleModal", function(e) {
					
					$(_VIDEO).get(0).pause();
				});