	
           // Check for the File API support.
           if (window.File && window.FileReader && window.FileList && window.Blob) {
               document.getElementById('chooseFile').addEventListener('change', handleFileSelect, false);
           } else {
               alert('The File APIs are not fully supported in this browser.');
           }



           var onFilePicked = function (event) {
               var input = event.target;
               var file = input.files[0];
               alert(file.type);
           };
           function onChange(event) {
               var file = event.target.files[0];
               var reader = new FileReader();
               reader.onload = function (e) {
                   // The file's text will be printed here
                   console.log(e.target.result)
               };

               reader.readAsText(file);
           }


           function handleFileSelect(evt) {

               var f = evt.target.files[0]; // FileList object

               var reader = new FileReader();
               // Closure to capture the file information.
               reader.onload = (function (theFile) {

                   return function (e) {
                       var binaryData = e.target.result;

                       //Converting Binary Data to base 64
                       var base64String = "" + window.btoa(binaryData);
                       var input = evt.target;
                       var file = f.type;
                       var size = f.size;  // taille image

                       var p = 1;

                       if (file.includes("jpeg") || file.includes("jpg")) {
                           alert(file + " -----> " + p);
                           document.getElementById('img').src = "data:image/jpeg;base64, " + base64String;
                           alert(document.getElementById('img'));
                           document.getElementById('format').value = "jpg";

                       } else {
                           if (file.includes("png")) {
                               document.getElementById('img').src = "data:image/png;base64, " + base64String;
                               document.getElementById('format').value = "png";
                           } else {
                               alert("Nous ne supportons pas des formats d'image autres que le png et le jpeg");
                               p = -1;
                           }
                       }
                       document.getElementById('tailleFichier').value = size;
                       if (p > 0) {

                           //showing file converted to base64
                           document.getElementById('file').value = base64String;

                           document.getElementById('img').style.display = "inline";
                           //alert('File converted to base64 successfuly!\nCheck in Textarea');


                       }
                   };
               })(f);
               // Read in the image file as a data URL.
               reader.readAsBinaryString(f);
           }

