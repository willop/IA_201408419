package com.example.realcameraia

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.realcameraia.ml.ModelProyectoIA
import com.example.realcameraia.ml.ModeloEntrenado
import com.example.realcameraia.ml.ModeloEntrenado2
import com.example.realcameraia.ml.ModeloFinal
import com.example.realcameraia.ml.ModeloFrutas
import com.example.realcameraia.ml.ModeloIA
import com.example.realcameraia.ml.ModeloLite
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.max


class MainActivity : AppCompatActivity() {

    //variables globales
    //para el layout de la imagen de la camara
    lateinit var textureView: TextureView
    lateinit var cameraManager: CameraManager
    lateinit var handler: Handler
    lateinit var cameraDevice: CameraDevice
    //para visualizar el tensorflow
    lateinit var imageView: ImageView
    lateinit var texttt: TextView
    lateinit var bitmap: Bitmap
    //lateinit var model: ModeloIA
    lateinit var model: ModeloEntrenado2
    lateinit var imageProcessor: ImageProcessor
    var paint = Paint()
    lateinit var labels:List<String>
    var colors = listOf<Int>(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //obtener permisos para la camara
        get_permisssion()

        labels = FileUtil.loadLabels(this,"labels.txt")
        imageProcessor =  ImageProcessor.Builder().add(ResizeOp(300,300,ResizeOp.ResizeMethod.BILINEAR)).build()
        //model = ModeloIA.newInstance(this)
        model = ModeloEntrenado2.newInstance(this)
        var handlerThread = HandlerThread("videoThread") //nombre del hilo de la camara
        handlerThread.start()
        handler = Handler(handlerThread.looper) //inicializa el hilo

        imageView = findViewById(R.id.imageView)
        texttt = findViewById(R.id.TextCamara)
        texttt.setBackgroundColor(Color.BLACK)

        textureView = findViewById(R.id.textureView) //busca el layout


        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camara()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {

            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false // por defecto
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                //al actualizarse
                bitmap = textureView.bitmap!!
                val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

                /*var image = TensorImage.fromBitmap(bitmap)
                image = imageProcessor.process(image)*/


                var tensorImage = TensorImage(DataType.FLOAT32)
                tensorImage.load(resizedBitmap)
                //tensorImage = imageProcessor.process(tensorImage)

                //tensorImage = imageProcessor.process(tensorImage)

                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                inputFeature0.loadBuffer(tensorImage.buffer)

                // Runs model inference and gets result
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

               /* var mutable = bitmap.copy(Bitmap.Config.ARGB_8888,true )
                val canvas = Canvas(mutable)*/
                /*
                val locations = outputs.locationsAsTensorBuffer.floatArray
                val classes = outputs.classesAsTensorBuffer.floatArray
                val scores = outputs.scoresAsTensorBuffer.floatArray
                val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray

                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888,true )
                val canvas = Canvas(mutable)


                val h = mutable.height
                val w = mutable.width
                paint.textSize = h/15f
                paint.strokeWidth = h/85f
                var x = 0
                scores.forEachIndexed { index, fl ->
                    x = index
                    x *= 4
                    if(fl > 0.5){
                        paint.setColor(colors.get(index))
                        paint.style = Paint.Style.STROKE
                        canvas.drawRect(RectF(locations.get(x+1)*w, locations.get(x)*h, locations.get(x+3)*w, locations.get(x+2)*h), paint)
                        paint.style = Paint.Style.FILL
                        canvas.drawText(labels.get(classes.get(index).toInt())+" "+fl.toString(), locations.get(x+1)*w, locations.get(x)*h, paint)
                    }
                }*/

                var maxIdx = 0

// Encuentra el índice de la máxima probabilidad
                val confidenceThreshold = 0.80f // Establece un umbral, por ejemplo, 50%

                val maxIndex = outputFeature0.indices.maxByOrNull { outputFeature0[it] } ?: -1
                val confidence = if (maxIndex != -1) outputFeature0[maxIndex] else 0f

                if (confidence >= confidenceThreshold) {
                    val labels = listOf("Manzana", "Banano") // Tus etiquetas
                    val className = labels[maxIndex]
                    if (className == "Manzana"){
                        texttt.setTextColor(Color.RED);
                    }else{
                        texttt.setTextColor(Color.YELLOW);
                    }

                    texttt.text = "Objeto: $className\nConfianza: ${confidence * 100}%"

                } else {
                    texttt.text = "Desconocido"
                    texttt.setTextColor(Color.WHITE)
                }

                runOnUiThread(){
                    imageView.setImageBitmap(bitmap)
                    //texttt.text = "Objeto: $className\nConfianza: ${confidence * 100}%"
                }

                //model.close()

            }
        }
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager //obtiene el primer dispositivo como su camara
    }


    override fun onDestroy() {
        super.onDestroy()
        model.close()
        cameraDevice.close()
    }

    //funcion para abrir la camara y mostrarla en el layout
    @SuppressLint("MissingPermission")
    fun open_camara(){
        cameraManager.openCamera(cameraManager.cameraIdList[0],object :CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                cameraDevice = p0

                var surfaceTexture = textureView.surfaceTexture
                var surface = Surface(surfaceTexture)

                var captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)//visualizacion del preview de la camara
                captureRequest.addTarget(surface)//lo manda a la surface

                cameraDevice.createCaptureSession(listOf(surface),object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(),null,null)
                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {

                    }
                },handler)
            }

            override fun onDisconnected(p0: CameraDevice) {

            }

            override fun onError(p0: CameraDevice, p1: Int) {

            }

        },handler)

    }

//funcion para solicitar permisos de acceso a la camara
    fun get_permisssion(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_DENIED){ //validar que los permisos no esten denegados
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101) //solicitar permisos para la camara
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
            get_permisssion()
        }
    }

}
