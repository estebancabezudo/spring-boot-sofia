# Sofia Template Engine

Sofia es una herramienta para crear contenido HTML estático a partir de archivos que pueden ser utilizados para
reutilizar código o simplemente para organizar de mejor manera el código.
Consiste en un motor creador de páginas, una API y una librería JavaScript.
Podemos reutilizar código HTML, CSS y JavaScript para facilitar la creación y el mantenimiento de nuestras páginas.
Con Sofía podemos escribir código organizado de la forma que queramos y mantener los archivos pequeños y, por lo tanto,
fáciles de mantener.
Permite fácilmente internacionalizar sitios.
Hace uso de archivos de configuración para definir constantes en archivos HTML, JavaSCript y CSS.

## Internacionalización

Sofia permite internacionalizar muy fácilmente un sitio. Supongamos que tenemos una página `/index.html` que queremos
internacionalizar.

```html

<div id="lista">
    <ul>
        <li>Tomates</li>
        <li>Lechugas</li>
    </ul>
</div>
```

Si queremos agregar el idioma inglés a nuestro sitio debemos de modificar el código HTML anterior para que Sofía pueda
procesarlo.

```html

<div id="lista">
    <ul>
        <li>
            <texts>
                <text language="es">Tomates</text>
                <text language="en">Tomatoes</text>
            </texts>
        </li>
        <li>
            <texts>
                <text language="es">Lechugas</text>
                <text language="en">Lettuces</text>
            </texts>
        </li>
    </ul>
</div>
```

La etiqueta `texts` especifica un grupo de textos a utilizar. Sofía convierte la etiqueta `texts` a una etiqueta `span`
y usa las etiquetas `text` internas para generar los archivos necesarios para el cambio de lenguaje.
Si no tenemos traducción o no tenemos varios lenguajes pondremos solo la etiqueta `text` y el sistema tomará esa
etiqueta para generar los textos para todos los lenguajes. También podemos colocar el texto sin la etiqueta `text` pero
no será parte de los textos del sistema y no podrán referenciarse con JavaScript mediante las variables de
internacionalización y formará parte del HTML final. Lo mejor es incluirla y dejar que el sistema se encargue de su
manejo. Si queremos acceder a su valor vamos a necesitar colocar un atributo `id` para poder referenciarla.

Si alguno de los lenguajes en la lista de lenguajes de la página se omite en la lista de etiquetas `text` se usará
inglés y si no está definido el texto para inglés, el primer lenguaje que se haya indicado. También podemos especificar
en la etiqueta `language` el valor `all` para que de forma explícita se entienda que el texto será utilizado en todos
los lenguajes.

Luego, si queremos cambiar a nuestra versión en inglés, simplemente llamamos al método `Core.loadLanguage('en')` y
Sofía lo hace por nosotros. O simplemente podemos usar una librería existente para mostrar una bandera del país donde se
utiliza el lenguaje y que podemos presionar para cambiar el lenguaje.

Cuando utilizamos Core, esto es, si lo incluimos en las librerías de la página, el lenguaje inicial mostrado por la
página se define al terminar de cargar la página. Si la página es cargada por primera vez se toma el lenguaje preferido
del navegador y si el lenguaje está en la lista de lenguajes definidos para el sitio se define este por defecto. CAbe
destacar que primero se busca el lenguaje con la variante, por ejemplo `es-uy`, si no se encuentra se busca solo `es`.

Si no se puede definir el lenguaje con los entregados por el navegador se utilizan los lenguajes del sitio. Primero se
busca si el inglés está en la lista de lenguajes y si no se encuentra se usa el primer lenguaje de la lista de
lenguajes definidos para el sitio.

Las siguientes veces que se acceda a alguna página del sitio se utilizará el lenguaje para la sesión, este es, el
definido por defecto la primera vez o el indicado por el usuario en un cambio de idioma.

A pesar de que la filosofía de utilizar fragmentos reutilizables para mantener los archivos con código fuente pequeños,
se puede dar el caso, o existe la preocupación de que el código html de un archivo sea muy grande a causa de los textos.
Sofía tiene una solución para esto. Podemos reescribir el código mostrado a una versión más pequeña y separarlos en dos
archivos. La parte de html que queda más pequeña.

```html

<div id="lista">
    <ul>
        <li id="tomatoes"></li>
        <li id="lettuce"></li>
    </ul>
</div>
```

Y el archivo con los textos para nuestro `/index.html`, cuál que llamaremos `/index.texts.json`

```json
{
  "tomatoes": {
    "es": "Tomates",
    "en": "Tomatoes"
  },
  "lettuces": {
    "es": "Lechugas",
    "en": "Lettuces"
  }
}
```

Así mantenemos los textos separados del código HTML haciendo que sea más fácil de leer y de actualizar.
Una de las características que tiene Sofía es que para agregar un nuevo lenguaje solo debemos agregar la entrada para el
lenguaje.

Además de

```json
{
  "tomatoes": {
    "es": "Tomates",
    "en": "Tomatoes",
    "fr": "Tomatoes"
  },
  "lettuces": {
    "es": "Lechugas",
    "en": "Lettuces",
    "fr": "Laitue"
  }
}
```

Para aquellas claves para textos que no tengan una traducción se utilizará inglés por defecto. Y si no está definido el
inglés se busca el primer idioma definido para los textos. Si no se encuentra obtendremos un error de creación.

Sofía entrega la lista de idiomas definidos en textos para el sitio en una variable a la cual podemos acceder desde
JavaScript usando la constante `siteLanguages` accesible de globalmente. De esta forma podemos saber que lenguajes
tenemos disponibles para presentar opciones al cliente.

## Spring profiles

Sofia utiliza los perfiles de spring para definir ciertos comportamientos que facilitan el desarrollo. A pesar de que en
spring se pueden utilizar varios perfiles, Sofia solo va a utilizar dos: dev y prod

Si bien se pueden especificar otros perfiles, Sofía solo va a modificar su comportamiento cuando encuentre uno de estos
dos, cualquier otro lo va a ignorar. Sin importar que perfiles se especifiquen, se va a comportar como si estuviera en
producción o en desarrollo.

Sin importar cuáles sean los perfiles especificados Sofía va a ejecutarse en modo desarrollo solo si se especifica el
perfil dev y no se especifica el perfil prod, en cualquier otro caso, para mantener la seguridad al máximo en caso de un
error en la configuración, se va a ejecutar en producción.

## Ciclo de vida de una petición

### Filtro creador de contenido estático

Sofia agrega a la cadena de filtros un filtro que se ejecuta en cada petición http. Este filtro,
llamado `StaticContentCreatorFilter`, realiza algunas tareas administrativas y principalmente se encarga de localizar y
eventualmente crear el archivo estático que se va a servir como respuesta. Cuando llega una petición el filtro primero
determina, utilizando el host de la petición, si es un sitio dentro de la lista de los sitios configurados en el
servidor. Si se encuentra en la lista lo guarda en la sesión para usos posteriores. De la misma forma determina a que
sitio pertenece ese host y lo guarda en la sesión para que quede a disposición en un futuro. Si alguno de estos dos
valores no son encontrados en la configuración el servidor regresa un 404 indicando que el recurso no se encuentra. No
se especifica más claramente el error para no dar información innecesaria al cliente.

Inmediatamente después el servidor realiza una comprobación de las rutas que debe ignorar en esta etapa y no tratar como
contenido estático a crear, más claramente, aquellas especificadas como API en el archivo de configuración `sofia.yml`.

Lo siguiente que se realiza es determinar que archivo va a localizar. Si la ruta de la petición no tiene un archivo
especificado, o sea que termina con la barra `/`, se agrega index.html y se modifica la ruta del request. Esto es
importante para evitar esta validación en etapas posteriores.

Si el servidor está en producción y el archivo existe en la ruta donde se encuentra el código generado simplemente se
continúa con la cadena de filtros. Si el servidor no encuentra el archivo o se encuentra en desarrollo se crean los
archivos necesarios a partir de los archivos fuentes, se copian en la ruta donde se localiza el código generado y se
continúa al siguiente filtro.

Debemos tener en cuenta que cuando estamos en desarrollo los archivos destino generados se sobrescriben sin ningún tipo
de advertencia.

## Archivos de Sofía

Sofía utiliza varios archivos para crear la página final que se va a mostrar al hacer una solicitud. No hay nada
especial en estos archivos, a excepción de las variables que se pueden insertar y una sutil diferencia en los archivos
HTML.
Sofía carga archivos de configuración y de texto con formato JSON, de estilo CSS y archivos JavaScript. Estos solo son
modificados para sustituir las variables de configuración o de plantilla y luego agregados al archivo general en el
orden de llegada. Sofía únicamente facilita esta agrupación para favorecer la reutilización y la internacionalización.

### Archivos de configuración

Sofía toma valores de configuración de diferentes archivos para generar un archivo JSON utilizado para sustituir
variables en el código fuente de forma de poder utilizar los valores tanto en CSS como en JavaScript y HTML. Cada vez
que el creador de archivos encuentra en el código fuente una instrucción con el siguiente
formato `#{application.colors.principal}` va a sustituir ese valor por el definido en los archivos de configuración
JSON. El valor del ejemplo se debe de encontrar en un archivo de configuración de la siguiente forma:

```json
{
  "application": {
    "colors": {
      "principal": "#546527"
    }
  }
}
```

Existen varios archivos de configuración y del orden de carga depende el valor que será tomado al terminar de cargar
todos los archivos.
Existe un archivo de configuración común a todo el sitio que se carga con cada página que se genera. Este debe
llamarse `commons.json` y debe ubicarse en el directorio base del código fuente.
Todos los archivos de configuración cargados en el proceso de generación de la página son accesibles en la página
utilizando la variable global `templateVariables`. De esta forma JavaScript puede acceder a estos valores.

### Archivos de textos

El formato general de un archivo de texto es el siguiente:

```json
{
  "key": {
    "es": "Texto en español",
    "en": "English text",
    "fr": "Texte en français"
  }
}
```

El número de idiomas puede variar a gusto del desarrollador y se pueden agregar y omitir textos si no se tienen.

Existe un archivo de texto común a todo el sitio que se carga con cada página que se genera. Este debe
llamarse `texts.json` y debe ubicarse en el directorio base del código fuente.

### Archivos HTML

Los archivos HTML utilizados por Sofía son iguales a los archivos HTML normales con algunas características especiales.
Primero hay que distinguir dos tipos. El archivo raíz o base, con el documento (si, haciendo referencia a document del
DOM) y
los fragmentos HTML.
El archivo base tiene el nombre del archivo que se desea crear y es el nombre del archivo que se solicita en la petición
http. Este debe tener un encabezado, ya que no será aceptado sin uno.
Los fragmentos son archivos referenciados dentro de un archivo HTML para ser incluidos dentro de este. No deben tener
encabezado, simplemente son archivos con formato HTML común, pero solo tienen tres partes.

```
<script>
  window.addEventListener('load', initLogin);
</script>

<style>
.formContainer {
  margin: 30px auto 60px auto;
  max-width: 350px;
}
</style>

<body>
  <div id="application">
    <div class="formContainer">
      <h1>
        <texts>
          <text language="es">Acceso</text>
          <text language="en">Login</text>
        </texts>
      </h1>
    </div>
  </div>
</body>
```

Como podemos ver las tres partes de las que hablamos son para JavaScript, CSS y el contenido de la etiqueta `body` será
colocada dentro del cuerpo de la etiqueta que llama el fragmento.

Cuando necesitemos crear componentes que utilizan textos, podemos utilizar métodos de Core para asignar el texto en
tiempo de ejecución para elementos creados al vuelo, esto es, aquellos que no existen en la página en el momento de
asignar los textos.
La lista de usuarios de la administración de Sofía, construye la lista con una librería llamada `users:1.00`. Esta
librería llama a la API y con la respuesta genera la lista cuando recibe los datos. En ese momento no sabemos si tenemos
los textos o no, ya que esa petición puede estar demorada.
Lo que se hace en este caso, es asignarle el texto usando el método `Core.setText(cellMenuItemEditElement, 'edit');`.
Este método toma el elemento al cual se le va a aplicar el texto y le asigna la clave `edit`, con la cual se localiza
el texto en los archivos de textos. Si el archivo de texto no se encuentra no se hace nada y se suscribe a un canal
llamado `core:setTexts` donde se publica en el momento de recibir los textos. De esta forma, cuando se tienen los
textos, el texto para ese elemento es asignado.
El enlace a editar se arma junto con el registro al recibir la lista de usuarios del servicio. No tenemos control
sobre cuando vamos a recibir la lista, tampoco tenemos control sobre cuando vamos a recibir los textos. Para esto
podemos suscribirnos a un canal que recibe un mensaje cuando se cambia el lenguaje y cambiar el lenguaje para el texto.

Hay otros métodos útiles con respecto a los textos y la internacionalización los cuales están especificados en la
descripción de la librería Core.

## Ciclo de vida de creación de ficheros estáticos

Para crear uná página Sofía toma una serie de archivos y los procesa para generar los archivos finales.
Es importante saber el mecanismo en que estos se leen, ya que de esto depende que variables, textos y estilos se
tomarán en el resultado final.

El primer archivo que se carga es el de configuración general. Esto quiere decir que los primeros valores cargados en el
archivo de configuración final van a ser los que se encuentren en este archivo. La relevancia de esto es que cualquier
otra configuración con una clave que se encuentre en este archivo no va a sustituir la entrada de este archivo. Al
agregar un archivo de configuración a la configuración general, si en la configuración ya existe una clave su valor no
será sustituido por el valor en el archivo de configuración que se está agregando.
El segundo archivo que se carga es el archivo general de textos, también se encuentra en la raíz de los archivos fuentes
y su nombre es `texts.json`. Luego de esto se carga el archivo general para estilos.
Este archivo es un archivo CSS con el nombre `commons.css` que se encuentra en el directorio raíz para los archivos
fuentes del sitio.

Luego se carga el archivo de configuración para la página que se está solicitando. Por ejemplo, si la página se
llama `listaDeEmpleados.html` el archivo de configuración se llamará `listaDeEmpleados.json`.
Luego de eso se carga el archivo de estilos para la página llamada, en este caso llamada `listaDeEmpleados.css`, el
archivo de JavaScript para página que en este caso se llama `listaDeEmpleados.js`, el archivo con los textos para la
página llamada cuyo nombre es `listaDeEmpleados.texts.json` y luego se lee el archivo HTML correspondiente o
sea `listaDeEmpleados.html`.

Este archivo HTML se procesa para obtener el archivo HTML final. Lo primero que se hace es crear un árbol con toda la
estructura de la página que se desea mostrar. Luego procesamos las etiquetas para encontrar y cargar librerías
utilizando la etiqueta `<link>`, JavaScript utilizando la etiqueta `<script>` y estilos utilizando la etiqueta `<style>`
, todas estas se deben colocar en la raíz del HTML cargan un archivo mediante un atributo `file`.
Luego de encontrar todas las etiquetas anteriormente mencionadas buscamos contenedores. Los elementos contenedores son
elementos de HTML que tienen un atributo `file` y no contienen elementos hijos. Mediante un atributo `file` cargan un
fragmento de HTML y lo agregan al arbol ya existente.

El último paso es procesar la etiqueta `<body>`, eventualmente encontrar grupos y agregar los permisos para la página en
la lista de permisos a procesar. Al finalizar todo este proceso se tiene la estructura completa del sitio en un solo
árbol que se usará para seguir el proceso y manipular nodos.

### proceso de etiqueta link

Las librerías se encuentran en un directorio `lib` en el directorio raíz del sistema definido con `basePath` en el
archivo de configuración `sofia.yml`. La ruta está definida por el nombre de la librería. Una librería solo puede
contener archivos JavaScript, CSS y textos. Todos los archivos que terminen en `.js`, `.css` y `texts.json` son
procesados y agregados al código final.
También podemos escribir nuestras propias librerías o colocar nuestras propias versiones de las existentes en la ruta
raíz del código fuente del sitio. El sistema busca primero en la ruta `libs` de la ruta base del sitio por la librería,
si no la encuentra en esa ubicación la busca en las librerías de sistema.
Las librerías se especifican en el código fuente que se va a utilizar para que quede explícitamente indicado que se va a
utilizar. Si se encuentran dos etiquetas de librerías que hacen referencia a la misma librería Sofía agrega únicamente
una. La idea es que se agregue la librería justo antes de usarse, de esta forma evitamos dejar código muerto al dejar de
usar una librería en un fragmento HTML que está siendo llamada en, por ejemplo, el archivo base.
Las librerías también pueden contener un directorio `images` con las imágenes que necesita la librería. La librería, o
cualquier parte del código, debe hacer referencia a estas imágenes mediante una ruta armada con el nombre de la
librería, ya que, para evitar conflictos se copia a carpetas creadas para estas imágenes.
Para una librería llamada `burgerMenu:1.00`, la ruta para la imagen `burger.svg`
sería `/images/burgerMenu/1.00/burger.svg`.

### Procesamiento de la etiqueta script

La etiqueta script simplemente toma el archivo indicado en el file y lo agrega a la lista de scripts. No hay
validaciones de tipo de archivo, de código ni nada en particular. Es por esto que se recomienda usar archivos terminados
en `.js` con código JavaScript únicamente como si fuera un archivo JavaScript regular.
La ruta indicada en el atributo `file` no debe ser absoluta y permite el uso de los directorios `.` y `..` para
referencias. Si se coloca una ruta absoluta la barra inicial será eliminada. Si por alguna razón se hace referencia a
una ruta fuera del directorio para los archivos fuentes para el sitio obtendremos un error de creación.

### Procesamiento de etiqueta style

La etiqueta style funciona de la misma forma que la etiqueta script con la diferencia de que se recomienda usar `.css`
para evitar confusiones y usar CSS como si fuera un archivo CSS regular.

### Procesamiento de los contenedores

Los contenedores son elementos que tienen un atributo `file` que indica un fragmento HTML a cargar. La ruta especificada
en el atributo debe ser relativa y puede usar `.` y `..` para referenciar directorios, pero no puede hacer referencia a
directorios fuera del directorio raíz de los archivos fuentes para el sitio.
Los fragmentos funcionan de la misma forma que el archivo HTML base. Por cada fragmento HTML se intentan cargar sus
correspondientes archivos para JavaScript, CSS, textos y configuración. Es por eso que, además del atributo `file` se
puede especificar un atributo `configurationFile` para cargar un archivo de configuración diferente al archivo por
defecto.
Luego se carga el contenido del archivo HTML y se procesa. Esto quiere decir que se van a tomar las diferentes etiquetas
y se va a distribuir el código según sean estilos, JavaScript o HTML. El contenido de la etiqueta `body` para el archivo
será colocado dentro de la etiqueta contenedora.

### Final del proceso general

Luego de procesar los archivos HTML se carga el archivo general de JavaScript llamado `commons.js` y se procesa, junto
con este, todo el código JavaScript que se encontró. La razón para que se procese en este punto es porque Sofía procesa
las librerías cargadas en todas las páginas para evitar repetir código.
Se procesan después todos los identificadores, se busca texto en el código y se asignan identificadores nuevos a todos
los textos encontrados para poder hacer referencia a ellos en el momento de internacionalizar.

La importancia del proceso de carga radica en que el orden en que se leen los diferentes archivos de configuración
determinan los valores a utilizar. Si se especifica un valor de configuración en el archivo que tiene el elemento
contenedor para una clave que definida en el contenedor, el valor va a ser el del archivo que contiene el
contenedor y no del fragmento HTML llamado. De la misma forma para las librerías. Si se especifica una clave en el
archivo de configuración del archivo que contiene la llamada a la etiqueta, se tomará el valor definido en el
archivo. Lo mismo sucede para los textos. Por lo tanto, si un texto está definido en una librería y queremos usar otro
simplemente lo ponemos en nuestro archivo de textos en común para el sitio.

Al finalizar todo el proceso, las páginas creadas son almacenadas en un directorio `sites` en la ruta raíz del
sistema utilizando una ruta generada a partir del nombre del sitio y la versión que se está utilizando.

## Depuración

Cada vez que se agrega código de un archivo css o js y el sistema está en modo desarrollo se indica el archivo y la
ubicación de donde se obtuvo. Para producción estas referencias se eliminan.

## Configuración del servidor

### sofia.yml

Es el archivo de configuración donde se especifican los diferentes sitios, sus nombres de host, las versiones y los
permisos.

### `basePath`

Es la ruta completa donde se encuentran todos los archivos que va a utilizar Sofia. Entre estos se encuentran las
librerías, los archivos origen y donde se ven a construir los archivos finales.

### `sourcePaths`

Una lista con las rutas completas que contienen los archivos de donde se van a tomar los archivos origen de donde se
crea el contenido del sitio.

### `sites`

La definición de los sitios que contiene el servidor

#### `name`

Una cadena con un nombre arbitrario para el sitio usado solo para identificar el sitio y crear las rutas necesarias para
trabajar.

#### `relpyAddress`

La dirección de correo que va a ser utilizada como origen para la comunicación. Todos los correos enviados por el
servidor van a tener esta dirección de correo en su campo from.

#### `hosts`

Una lista con los hosts del sitio y su versión separado por dos puntos. El host es utilizado para localizar los archivos
dentro del servidor y la versión indica que versión se va a utilizar para ese host. De esta forma es posible tener
diferentes versiones del sitio en diferentes hosts.

#### `api`

Indica las rutas que no van a ser utilizadas para contenido estático y serán mapeadas a controladores. Indican las rutas
donde se encuentran API. Algunos de estos valores son necesarios para el funcionamiento del servidor y deben colocarse
en la mayoría de los casos. Algunas como `/logout` podría ser omitida en caso de que se sustituya el endpoint por una
versión personalizada o en el caso de que se quiera usar otra API para realizar las operaciones que se ofrecen por
defecto.

#### `permissions`

Una lista de permisos para páginas en el sitio. Cada uno de los elementos indican el grupo, el usuario, el permiso y la
ruta completa a la página separados por dos puntos. Es posible indicar finalizar la ruta con `**` para indicar que son
todas las páginas en cierto directorio. Los permisos especificados explícitamente aquí no pueden ser sobreescritos por
un permiso especificado en el cuerpo de una página.

### Ejemplo de archivo no tan completo

```yaml
basePath: /home/esteban/sofia/system
sourcePaths:
  - /home/esteban/sofia/sources
sites:
  - name: localhost
    mail:
      replyAddress: noreply@mail.server.com
    hosts:
      - localhost:1
      - beta.localhost:2
    api:
      - /logout
      - /v1
    permissions:
      - all:all:grant:/login.html
      - all:all:grant:/index.html
      - all:all:grant:/texts/**
      - all:all:grant:/images/**
      - all:all:grant:/fonts/**
  - name: example.com
    domainName: example.com
    hosts:
      - example.com:1
      - local.example.com:1
      - beta.example.com:2
      - www.example.com:1
```

## Configuración de Spring boot

En realidad no se necesita ninguna configuración de spring para funcionar. Hay algunos valores por defecto configurados
en el archivo `default.properties` que permiten que el servidor funcione sin problemas sin configurar nada. Por defecto
las operaciones con bases de datos necesarias se realizan en memoria por lo que no sería necesario configurar base de
datos. Un servidor en producción no necesitaría configuración, si vamos a trabajar de forma local tal vez sería bueno
configurar login y perfiles.

```yaml
logging:
  level:
    root: DEBUG
spring:
  profiles:
    active: dev
```

Pero si necesitamos utilizar permisos vamos a tener que crear una base de datos con cierta estructura. El archivo de
configuración entonces va a contener alguna configuración más para base de datos.

```yaml
logging:
  level:
    root: DEBUG
log4j:
  logger:
    org.springframework.jdbc.core: TRACE
spring:
  profiles:
    active: dev
  autoconfigure:
    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
  datasource:
    url: jdbc:mysql://localhost:3306/sofia
    username: sofia
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### SSL

Podemos utilizar SSL hay que configurarlo como si no tuviera corriendo Sofía. Solamente debemos agregar la
configuración al archivo `application.yml` y colocar nuestro archivo con el almacén de claves.

```yaml
server:
  ssl:
    key-store-type: PKCS12
    key-store: classpath:keystore.p12
    key-store-password: password
    key-alias: my_alias
```

Si vamos a probar en nuestra máquina local, podemos utilizar `keytool` para crear el almacén de datos sobre Linux. Si
vamos a usar el servidor en Internet necesitamos agregar los dominios que definamos.

```shell
keytool -genkey -alias serverkey -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -keystore keystore.p12 -storepass password -ext san=dns:localhost
```

Si necesitamos agregar un certificado emitido por una entidad certificadora debemos convertirlo y agregarlo y para esto
podemos usar `openssl`

```shell
openssl pkcs12 -export -in certificate.crt -inkey private.key -out keystore.p12 -name "my_domains"
```

### Conector AJP

Para crear un conector AJP que podamos utilizar con Apache Web Server simplemente agregamos la configuración al
archivo `application.yml` y si los dos valores están presentes automáticamente se creará el conector. Luego solo debemos
configurar el Apache Web server para que funcione con el conector.

```yaml
server:
  ajp:
    port: 8009
    redirectPort: 8443
```

Esta configuración es propia de Sofía y no aparecerá en la documentación de Spring boot. La clase encargada de crear el
conector es `net.cabezudo.sofia.config.AJPConnectorSetup`.

## Administración de correos electrónicos

## Administración de nombres de hosts

## Administración de sitios

## Usuarios, grupos y cuentas

Sofía utiliza usuarios para registrarse en el sistema. Un usuario es una dirección de correo que posee una contraseña y
una o varias cuentas en el sistema. Para fines prácticos, la mayoría de las veces podemos decir que un usuario es una
dirección de correo y una contraseña con cierto acceso al sistema.
El control de acceso al sistema se realiza mediante grupos. Un usuario puede acceder a uná página protegida si pertenece
a cierto grupo definido para esa página. Los grupos son nombres arbitrarios que no tienen por qué ser definidos en
ningún sitio.
Existen nombres de grupos reservados para uso interno y administración. Estos son `admin`, definido para los
administradores del sitio. Tiene accso a todos los usuarios y configuración del sitio. El otro grupo es `user`, definido
internamente para los usuarios registrados en el sistema. Cuando un cliente accede al sistema automáticamente pertenece
al grupo `user`.

Por defecto una página permite el acceso a todos los clientes. Si queremos limitar el acceso a una página basta con
especificar los nombres de los grupos que pueden acceder a la página. Estos nombres pueden ser elegidos arbitrariamente
teniendo en cuenta los grupos reservados.

```html

<body groups="profesores,directores">
</body>
```

En el ejemplo, solo van a poder acceder a esta página los usuarios que pertenezcan al grupo `profesores` y al grupo
de `directores`. Ahora, lo único que hay que hacer es asignarle ese grupo a los usuarios que queremos que puedan acceder
a esa página. Esto lo podemos hacer con la herramienta de administración del sitio.

Al crear un usuario también se crea una cuenta asociada al usuario. El usuario nuevo es dueño de esta cuenta y tiene
todos los permisos posibles para esa cuenta. Las cuentas existen porque existen casos en los cuales la organización de
la aplicación necesita de varios espacios para los usuarios.
Supongamos que tenemos una aplicación para alojar páginas personales. Nos registramos y vamos a diseñar y modificar
nuestra página sin problemas, pero si necesitamos ayuda de un tercero debemos darle permisos para que pueda hacer
ciertas acciones limitadas o no en nuestro sitio. El usuario de esta persona tiene un grupo `asistentes` para cambiar
valores en nuestra página. El problema surge cuando esta persona también tiene su página. La forma que tiene Sofia de
solucionar este problema es mediante cuentas. Un usuario tiene una cuenta propia con permisos ilimitados, pero también
puede tener permisos para otras cuentas en el sistema. Los grupos que se definen para los usuarios son relativos a la
relación entre un usuario y una cuenta, no solamente al usuario o a la cuenta.
Si no hay una cuenta definida por defecto para un sitio, cada vez que se crea un usuario de alguna, se crea su propia
cuenta y no tiene acceso a ninguna otra. Es importante tener presente esto al momento de diseñar una aplicación que va a
hacer uso de cuentas. Si la aplicación solamente va a utilizar permisos de acceso a páginas, las cuentas no tiene
relevancia.

### Acceso al sistema y cuentas

Cuando un usuario accede al sistema lo primero que se valida es si hay una cuenta definida para esa sesión. Si no hay
cuenta definida para esa sesión se busca si hay una cuenta definida en las preferencias del usuario, esto es,
generalmente, la cuenta con la que el usuario accedió la última vez al sistema. Si el usuario no tiene una cuenta en sus
preferencias se utiliza la propia cuenta del usuario.

Si hay una cuenta definida para esta sesión el sistema comprueba que el usuario tenga acceso a esa cuenta. Si el usuario
tiene acceso a esa cuenta se utiliza la cuenta en la sesión, si el usuario no tiene acceso a la cuenta se utiliza la
cuenta en las preferencias de usuario. Si el usuario no tiene una cuenta en sus preferencias se selecciona la propia
cuenta del usuario.

## Administración de países

## Seguridad

Sofia hace uso de del sistema de seguridad que brinda spring y tiene resuelto muchos de los problemas que vamos a tener
a la hora de querer utilizar control de acceso a una aplicación mediante usuarios.
Ya está implementado un login, un registro mediante Google y Facebook y una administración de usuarios.
Las páginas pueden protegerse mediante grupos y la API está protegida mediante cookies o JWT.
Para activar el control de acceso debemos de agregar las rutas necesarias a la lista de rutas definidas como API en el
archivo de configuración.
Si queremos solo Web login podemos agregar `/v1/login` y `/logout`. Agregando esto podemos hacer una distinción entre
las personas registradas y las que no mediante el grupo `user`. Si queremos refinar los permisos vamos a necesitar
agregar toda la API de Sofía con la ruta `/v1`.

### OAuth2

Para agregar autorización mediante OAuth2 debemos agregar la ruta `/oauth2` y si queremos agregar botones sociales
debemos agregar la ruta `/login/oauth2/code` y especificar la configuración en `application.yml` para Spring Security.

```yaml
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: 51xxxxxxxx21-ag30xxxxxxxxxxx2ukq646mfouxxxxxm.apps.googleusercontent.com
            client-secret: GOCSGS-RE2cTxAxxxxxxxxxxII2MTH2v-ue
            scope: profile,email
```

## Envío de correo

Si necesitamos enviar correos podemos agregar en la configuración los valores del servidor de correo. Actualmente,
existen dos formas de enviar correo, una es usando `JavaMailSender` de Spring (sin implementar) y MailJet. Para usar
JavaMailSender debemos agregar la configuración en el archivo de configuración `application.yml`.

```yaml
spring:
  mail:
    host: your_email_host
    port: your_email_port
    username: your_email_username
    password: your_email_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

Para utilizar MailJet deberemos agregar nuestras claves de acceso a la API en el archivo de
configuración `application.yml`.

```yaml
spring:
  mail:
    mailJet:
      apiKey:
        public: c1a2xxxxxxxxxxxxxxx520db9ec5e79b
        private: afc6xxxxxxxxxxxxxxx7ba80727f4788
```

Debemos tener en cuenta que si agregamos las dos configuraciones simultáneamente se utilizará `JavaMailSender`. Por más
información podemos ver la clase `net.cabezudo.sofia.config.mailMailClientConfiguration`.

## Preferencias de usuario

## Problemas conocidos

Si no se cierran las etiquetas correctamente, ya sea autocerrada o no se producen problemas inesperados que no son
detectados por el parser. Un caso puede ser una etiqueta IMG, la cual normalmente no se cierra. Si esta no se cierra
genera dos cierres para la siguiente etiqueta generando un html inválido.