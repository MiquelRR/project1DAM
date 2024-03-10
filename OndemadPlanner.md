#OnDemandPlanner 
##Planificador para trabajos a demanda para talleres y fábricas.

### El Problema

Partimos de la base de la estructura típica de un taller, o de una empresa que fabrica a demanda necesita planificar su producción. Desde la dirección bien sea el empresario único o una oficina técnica necesitan pasar de el pedido hasta la producción y poder controlar básicamente dos puntos:
* Los plazos de entrega
* La eficiencia de los recursos en tiempos

##### Que no pretende el sistema

Esta versión del software no pretende gestional agendas de perosonal no ligado directamente a la prodccion, como son administrativos, prevención de riesgos, y aunque los que pretende es facilitar el trabajo de la oficinatécnica, tampoco planifica su tiempo.

Esta version del software no tiene en cuenta el uso del material sobre el pedido, por ende tampoco su rentabilidad económica. Pero todo se andará.

Tampoco tiene en cuenta el control de materia prima ni el control de material elaborado o semi-elaborado, pero todo se andará.

### A que tipo de empresa está dirigido
Este sistema de organización esta planteado para controlar las agendas y agendar los procesos de fabricación de una empresa que cuenta con personas que producen y pueden contar con máquinas que les ayudan a ello. 
Ejemplos de talleres o fábricas que pueden implementar este modelo serían: 

- Una empresa de rótulos
- Una empresa de artes gráficas
- Un taller de cerrajería
- Un taller de reparación de vehículos
- Una carpinteria
- Una fábrica de muebles a demanda
- Un taller de impresión digital
- [...]

A continuación aparecen los conceptos de gestión sobre los que se basa el modelo; si estos conceptos cuadran o pueden cuadrar en tu estructura de fabricación, esta puede ser candidata a la implantación.

### El pedido
El cliente demanda un pedido con uno o deferentes productos (o servicios) de cada artículo, se pueden pedir una o más copias.
Como norma general, esperamos hacer la entrega/montaje de una vez. Si esto no es así, será mejor dividir el pedido del cliente.

### El tipo de producto
Empezamos con un ejemplo:
**Mesa de melamina blanca** es un tipo de producto.
una mesa de melamina, sea del tamaño que sea tiene este proceso: 
- Se corta un tablero de melamina blanca.
- Se cantea con cinta de color blanco 
- Se le colocan x patas (normalmente 4) metálicas blancas y sus tapones
- Cortamos las planchas de cartón en una maquina que hace cajas a medida
- Se empaqueta con cartón cortado a medida y  rellenos de espuma de polietileno

En este modelo, un **tipo de producto** recoge todos aquellos productos que siguen el mismo proceso de fabricación, y usan los mismos materiales aunque puedan tener cantidades diferentes.

*En esta versión del software vamos a obviar el consumo de materia prima, pero lo dejamos preparado para su ampliación.

**Un producto** de este tipo seria *"una mesa de melamina blanca de 120x60 cm"*
**Un pedido** podria incluir *cinco mesas de melamina blanca 120x60 y una de 200x60*, este pedido tendria un total de 6 productos, todos del mismo **tipo** pero de diferente **modelo**, tambien podria incluir productos de otros tipos, durante el proceso de **agendación** veremos la compatibilidad de los mismos y si merece la pena separarlos.

### El proceso
El proceso es una característica del tipo de producto. EL proceso define **tareas** donde se pueden usar **materiales** o **máquinas**, pero siempre necesitan un operario **responsable** y opcionalmente otros operarios **implicados**


En el ejemplo del tipo **Mesa de melamina blanca** el proceso tendria las siguientes **tareas**:
- **Tarea** *corte*,  **responsable** *Vicente* con **capacidad** de realizar este tipo de tarea y adiestramieto en **maquina** *escuadradora*, en esta tarea se consumirá *tablero de melamina blanca* del almacen,
- **Tarea** *corte cnc* del embalaje **responsable** *Luis* , se consumira *cartón* y *espuma de poliuretano* y se usará la **maquina** *Zünd*
- **Tarea** *canteado*, que en el momento de programar, la podrá hacer tanto Vicente como Luis, pues los dos tienen **capacidad** y no se necesita maquina para ello.
- **Tarea** *embalaje* , esuna tarea de baja ualificacion que todos los operarios tienen **capacidad** se elige por defecto el de menor coste.

De estas tareas , las dos de corte se pueden iniciar simultáneamente pues no se necesita el producto para hacerla, despues el canteado y finalmente el embalaje.


















