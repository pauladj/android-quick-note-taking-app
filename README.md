<div align="center"><img src="img/logo.png" width="300"/></div>

<h3 align="center">Easy and quick note-taking app</h3>

<div align="center">
    <img src="https://img.shields.io/badge/Android-≥5.0-green?logo=android" />
    <img src="https://img.shields.io/badge/Java-v1.8-orange?logo=java" />
    <img src="https://img.shields.io/badge/PHP-v7.x-blue?logo=php" />
</div>

### Descripción

La aplicación Notes permite a los usuarios escribir, guardar y editar sus propias notas, evitando así que se les olvide cualquier información que deseen recordar.

[Documentación app versión 1](https://github.com/pauladj/quick-note-taking-app/blob/master/Documentaci%C3%B3n%20v1.0.pdf)

[Documentación app versión 2](https://github.com/pauladj/quick-note-taking-app/blob/master/Documentaci%C3%B3n%20v2.0.pdf)

### Herramientas y conceptos utilizados

- Uso de **Listviews** personalizados o de **RecyclerView+CardView** para mostrar listados de elementos con diferentes características.
- Usar una **base de datos local**, para listar, añadir y modificar elementos y características de cada elemento. 
- Uso de una **base de datos remota** para el registro y la identificación de usuarios. 
- Uso de **diálogos**. 
- Usar **notificaciones** locales. 
- Uso de mensajería FCM (**Firebase Cloud Messaging**). 
- Usar **intents** implícitos para abrir otras aplicaciones, contactos, etc.
- Permitir que una misma funcionalidad se comporte de manera distinta dependiendo de la orientación (o del tamaño) del dispositivo mediante el uso de **Fragments**. 
- Hacer la aplicación **multiidioma**.
- Uso de **ficheros** de texto. 
- Uso de **Preferencias**, para guardar las preferencias del usuario en cuanto a mostrar/esconder cierta información, elegir colores para la aplicación, o cualquier otra cosa relacionada con la visualización de la aplicación. 
- Crear **estilos y temas propios**, para personalizar fondos, botones, etc. 
- Pantalla de **login** (y registro), para guardar credenciales de usuario (nombre de usuario – contraseña) en la base de datos local
- Captar **imágenes** desde la cámara, guardarlas en el servidor y mostrarlas en la aplicación. 
- Controlar que la aplicación se comporte correctamente y no se pierda información, aunque en medio de la ejecución recibamos una llamada telefónica, giremos el móvil, etc. No se impide que la aplicación se gire. Se controla la pila de actividades para evitar incoherencias.

### Capturas de pantalla
<div align="center"><img src="img\lista-notas.png"  width="250" />&nbsp;&nbsp;&nbsp;&nbsp;<img src="img\nota.jpg" width="250"/></div>

### Índice de funcionalidades

- Crear notas
- Editar notas
- Borrar notas
- Crear etiquetas
- Borrar etiquetas
- Cambiar la configuración
- Recibir notificaciones
- Mostrar notas recientes primero
- Cambiar lenguaje
- Guardar posición de notas en el mapa
- Ver mapa
- Añadir nota al calendario
- Subir notas a Google Drive:
- Crear notas cortas o rápidas.

### Archivos del código fuente

- **web-php:** Código del servidor que gestiona las llamadas externas de la aplicación.  Por ejemplo, el inicio de sesión o registro contra una base de datos remota.
- **NotesApp/app/src/main** Código fuente de la aplicación de Android.
- **Documentación v1.0.pdf**: Informe con tutorial de uso entregado para la primera versión de la aplicación.
- **Documentación v2.0.pdf**: Informe con tutorial de uso entregado para la segunda versión de la aplicación.

### Miembros del equipo

La aplicación se ha realizado por Paula de Jaime (<a href="https://github.com/pauladj">@pauladj</a>).

