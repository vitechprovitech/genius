const MAX_WIDTH = 320;
const MAX_HEIGHT = 180;
const MIME_TYPE = "image/jpeg";
const QUALITY = 0.7;

const input = document.getElementById("file-to-upload");
input.onchange = function (ev) {
  const file = ev.target.files[0]; // get the file
  var fo = file.type;
  var FILE_INCLUDES_JPG=fo.includes("jpg") ;
  var FILE_INCLUDE_JPEG=fo.includes("jpeg");
  var FILE_INCLUDE_PNG=fo.includes("png") ;
  const blobURL = URL.createObjectURL(file);
  const img = new Image();
  img.src = blobURL;
  img.onerror = function () {
    URL.revokeObjectURL(this.src);
    // Handle the failure properly
    console.log("Cannot load image");
  };
  img.onload = function () {
    URL.revokeObjectURL(this.src);
    const [newWidth, newHeight] = calculateSize(img, MAX_WIDTH, MAX_HEIGHT);
    const canvas = document.createElement("canvas");
    canvas.width = newWidth;
    canvas.height = newHeight;
    const ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0, newWidth, newHeight);
    canvas.toBlob(
      (blob) => {
        // Handle the compressed image. es. upload or save in local state
         var reader = new FileReader();
         reader.readAsDataURL(blob); 
         reader.onloadend = function() {
             /**************************************/
             var binaryData = reader.result;
             
             //Converting Binary Data to base 64
             var base64String =binaryData.split(",")[1];
             var input = ev.target;
             var p = 1;    
             var file = blob.type;
             var size = blob.size;  // taille image

             var p = 1;
             
             if (FILE_INCLUDES_JPG|| FILE_INCLUDE_JPEG) {
                 alert(file + " -----> " + p);
                 document.getElementById('img').src =binaryData;
                 //alert(document.getElementById('img'));
                 document.getElementById('format').value = "jpg";

             } else {
                 if (FILE_INCLUDE_PNG) {
                     document.getElementById('img').src = binaryData;
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
             /*****************************************/
         }
      },
      MIME_TYPE,
      QUALITY
    );
    //document.getElementById("root").append(canvas);
  };
};

function calculateSize(img, maxWidth, maxHeight) {
  let width = img.width;
  let height = img.height;

  // calculate the width and height, constraining the proportions
  if (width > height) {
    if (width > maxWidth) {
      height = Math.round((height * maxWidth) / width);
      width = maxWidth;
    }
  } else {
    if (height > maxHeight) {
      width = Math.round((width * maxHeight) / height);
      height = maxHeight;
    }
  }
  return [width, height];
}

// Utility functions for demo purpose

function displayInfo(label, file) {
  const p = document.createElement('p');
  p.innerText = `${label} - ${readableBytes(file.size)}`;
  document.getElementById('root').append(p);
}

function readableBytes(bytes) {
  const i = Math.floor(Math.log(bytes) / Math.log(1024)),
    sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

  return (bytes / Math.pow(1024, i)).toFixed(2) + ' ' + sizes[i];
}