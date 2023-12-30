# Wilfred Stewart Perez Solorzano
# 201408419
# Manual Técnico - Aplicación de Reconocimiento de Frutas

## Índice

- [Introducción](#introducción)
- [Arquitectura de la Aplicación](#arquitectura-de-la-aplicación)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Instalación del Entorno de Desarrollo](#instalación-del-entorno-de-desarrollo)
- [Estructura del Código Fuente](#estructura-del-código-fuente)
- [APIs y Dependencias](#apis-y-dependencias)
- [Pruebas y Validación](#pruebas-y-validación)
- [Despliegue y Mantenimiento](#despliegue-y-mantenimiento)
- [Contacto para Desarrolladores](#contacto-para-desarrolladores)

## Introducción

Este documento proporciona una visión técnica detallada de la aplicación de Reconocimiento de Frutas, diseñada para identificar manzanas y bananos en tiempo real utilizando la cámara de dispositivos Android.

## Arquitectura de la Aplicación

Descripción general de la arquitectura de la aplicación, incluyendo:

- **Flujo de Datos**: Cómo la aplicación procesa la entrada de la cámara y devuelve resultados.
- **Componentes Principales**: Descripción de los módulos principales de la aplicación.

## Tecnologías Utilizadas

- **Android SDK**: Versión utilizada y características relevantes.
- **Librerías de Visión por Computadora**: Herramientas y frameworks para el procesamiento de imágenes.
- **Machine Learning**: Detalles sobre el modelo de aprendizaje automático utilizado.
- **Colab**: Google Colab es una plataforma gratuita que ofrece un entorno de Jupyter Notebook en la nube con acceso gratuito a recursos computacionales, incluyendo GPUs y TPUs. Es ideal para entrenar modelos de Machine Learning, especialmente para aquellos que requieren alto poder computacional.

## Pasos para el Entrenamiento de un Modelo en Google Colab

### Paso 1: Configuración del Entorno

1. **Accede a Google Colab**: Ve a [Google Colab](https://colab.research.google.com/) y inicia sesión con tu cuenta de Google.
2. **Crea un Nuevo Notebook**: Haz clic en `Nuevo Notebook` para empezar con un entorno limpio.

### Paso 2: Preparación de los Datos

1. **Carga de Datos**: Puedes cargar datos desde tu Google Drive, GitHub o mediante la subida directa. Por ejemplo, para montar Google Drive, usa:

   ```python
   from google.colab import drive
   drive.mount('/content/drive')
   ```

    ```
    import tensorflow as tf
    model = tf.keras.models.Sequential([
    tf.keras.layers.Dense(128, activation='relu'),tf.keras.layers.Dropout(0.2),tferas.ayers.Dense(10, activation='softmax')
    ])
    ```
2. **Compilacion del modelo**
    ```
    model.compile(optimizer='adam',
                loss='sparse_categorical_crossentropy',
                metrics=['accuracy'])
    ```
## Instalación del Entorno de Desarrollo

Pasos para configurar el entorno de desarrollo, incluyendo:

- Instalación de Android Studio.
- Configuración de emuladores o dispositivos de prueba.

## Estructura del Código Fuente

Descripción de la estructura del repositorio de código fuente, incluyendo:

- **Directorios Principales**: Descripción de los paquetes y su contenido.
- **Clases Importantes**: Descripción de las clases clave y su funcionalidad.

## APIs y Dependencias

Listado y explicación de las APIs y librerías de terceros utilizadas en la aplicación.

## Importar un Archivo de TensorFlow Lite en Android Studio

Este documento describe los pasos para importar y utilizar un archivo de modelo TensorFlow Lite (`.tflite`) en un proyecto de Android Studio.

## Pasos para la Importación

### Paso 1: Configurar el Proyecto en Android Studio

Asegúrate de tener Android Studio instalado y de que tu proyecto esté configurado correctamente.

### Paso 2: Agregar la Dependencia de TensorFlow Lite

1. Abre el archivo `build.gradle` (nivel de módulo) en tu proyecto de Android Studio.
2. Agrega la siguiente dependencia en la sección `dependencies`:

   ```gradle
   implementation 'org.tensorflow:tensorflow-lite:0.0.0-nightly'


## Carga de modelo en android Studio
```
import org.tensorflow.lite.Interpreter;

// ...

try {
    Interpreter tflite = new Interpreter(loadModelFile(), null);
} catch (Exception e) {
    e.printStackTrace();
}

// ...

private MappedByteBuffer loadModelFile() throws IOException {
    AssetFileDescriptor fileDescriptor = this.getAssets().openFd("ModeloFrutas.tflite");
    FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
    FileChannel fileChannel = inputStream.getChannel();
    long startOffset = fileDescriptor.getStartOffset();
    long declaredLength = fileDescriptor.getDeclaredLength();
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
}
```

## Pruebas y Validación

- **Pruebas Unitarias**: Cómo ejecutarlas y añadir nuevas.
- **Pruebas de Integración**: Procedimientos para validar el funcionamiento conjunto de los componentes.

## Despliegue y Mantenimiento

Guía para el despliegue de la aplicación y prácticas recomendadas para su mantenimiento.

## Contacto para Desarrolladores

## XML de la interfaz grafica

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">


    <TextureView
        android:id="@+id/textureView"
        android:layout_width="match_parent"
        android:layout_height="679dp" />


    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="682dp"
        android:background="#000" />


    <TextView
        android:id="@+id/TextCamara"
        android:layout_width="match_parent"
        android:layout_height="101dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentEnd="true"
        android:layout_alignParentBottom="true"
        android:layout_marginStart="0dp"
        android:layout_marginEnd="0dp"
        android:layout_marginBottom="0dp"
        android:background="@color/white"
        android:fontFamily="serif"
        android:textAlignment="center"
        android:textSize="24sp" />

    <EditText
        android:layout_width="261dp"
        android:layout_height="37dp"
        android:layout_alignParentStart="true"
        android:layout_alignParentTop="true"
        android:layout_marginStart="1dp"
        android:layout_marginTop="8dp"
        android:background="@drawable/bg_name"
        android:text="   Wilfred Perez - 201408419"
        android:textColor="@color/black" />
</RelativeLayout>
```

Información de contacto para desarrolladores interesados en contribuir o reportar problemas:

- **Email**: wilfredp159@gmail.com 
- **GitHub**: [Repositorio de la Aplicación](https://github.com/IA_201408419)
