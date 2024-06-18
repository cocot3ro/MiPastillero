# Estudo preliminar

## 1. Descrición do proxecto
A aplicación ten como obxectivo proporcionar aos usuarios unha ferramenta práctica para xestionar e controlar de preto o seu réxime de medicación. A aplicación centrarase na xestión dos medicamentos ao longo do tempo, permitindo aos usuarios facer un seguimento dos medicamentos que necesitan tomar diariamente.

### 1.1. Xustificación do proxecto
**Redución de erros na toma de medicamentos:** Os erros na toma de medicamentos poden ter consecuencias graves. A aplicación proporcionará recordatorios precisos e claros, minimizando os riscos de confusión ou toma incorrecta de medicamentos.

**Facilita o manexo en réximes complexos:** moitos pacientes deben seguir varios medicamentos con diferentes horarios e doses. A aplicación axudará aos usuarios a realizar un seguimento dos réximes de medicamentos complexos dun xeito sinxelo e organizado.

**Axuda aos coidadores:** a aplicación pode ser útil para os coidadores, familiares e amigos que axudan a xestionar a medicación doutras persoas. Facilita a coordinación e supervisión da toma de medicamentos dos seres queridos.

**Gravación e análise de datos de medicamentos:** a aplicación permite aos usuarios manter un historial completo dos seus medicamentos. Isto é útil para os profesionais sanitarios, xa que poden analizar estes datos para tomar decisións informadas sobre o tratamento.

### 1.2. Funcionalidades do proxecto
**Notificacións de recordatorio:** a aplicación enviará notificacións personalizadas aos usuarios para lembrarlles cando deben tomar os seus medicamentos, contribuíndo así a garantir o correcto cumprimento do tratamento.

**Xestión de medicamentos:** os usuarios poderán engadir, eliminar e editar medicamentos na súa lista personalizada. Poderán introducir detalles como o nome do medicamento, a dosificación, a frecuencia e o momento da administración.

**Rexistro histórico:** a aplicación manterá un rexistro histórico dos medicamentos tomados polo usuario, o que permitirá un seguimento preciso do cumprimento do tratamento e facilitará a comunicación cos profesionais sanitarios.

**Lista de favoritos:** proporcionarase unha función de lista de favoritos para que os usuarios poidan marcar os medicamentos que toman con máis frecuencia, facilitando o acceso rápido e sinxelo.

**Información detallada sobre os medicamentos:** a aplicación ofrecerá información detallada sobre cada medicamento, incluíndo doses recomendadas, posibles efectos secundarios, interaccións con outros medicamentos e calquera outra información relevante.

### 1.3. Estudo de necesidades
**Adherencia á medicación:** axuda aos pacientes a seguir os seus réximes de medicación de forma máis consistente e adecuada, o que pode mellorar os resultados do tratamento e reducir as complicacións de saúde.

**Información sobre medicamentos:** proporcione aos pacientes información detallada sobre os seus medicamentos, incluíndo doses recomendadas, efectos secundarios, interaccións e moito máis, para tomar decisións máis informadas.

**Historial de medicamentos:** permite aos usuarios rastrexar e acceder a un historial dos seus medicamentos ao longo do tempo, o que é útil tanto para o paciente como para os profesionais sanitarios.

**Facer a vida das persoas maiores máis fácil:** axudar aos maiores a manter o ritmo dos seus réximes de medicamentos, o que pode ser un reto a medida que envellecen e se enfrontan a tratamentos máis complexos.

**Personalización e Flexibilidade:** Adaptarse ás necesidades individuais dos usuarios e permitir a configuración de recordatorios e seguimento segundo os seus tratamentos específicos.

**Accesibilidade tecnolóxica:** ofrece unha solución de xestión de medicamentos en dispositivos Android, que son amplamente accesibles para unha variedade de usuarios.

**Rexistro de historia clínica:** rastrexa con precisión o historial médico para axudar aos usuarios e profesionais sanitarios a tomar decisións informadas sobre o tratamento.

### 1.4. Persoas destinatarias
**Pacientes:** Este é o grupo principal de destinatarios. Os pacientes que precisen tomar medicamentos con regularidade, xa sexa para tratar enfermidades crónicas, agudas ou preventivas, beneficiaranse directamente da aplicación. Isto inclúe persoas de todas as idades, desde mozos con tratamentos a curto prazo ata persoas maiores con réximes máis complexos.

**Coidadores:** Os coidadores, como familiares, amigos ou profesionais da saúde, tamén son destinatarios importantes. A aplicación permitiralles brindar apoio e seguimento aos pacientes, o que é esencial nas situacións nas que se require a administración e medicamentos.

### 1.5. Modelo de negocio
Dado que o obxectivo principal é ofrecer unha solución accesible e útil para a xestión dos medicamentos, a aplicación ofrecerase de xeito totalmente gratuíto a todos os usuarios. Esta decisión baséase en varios factores:

**Accesibilidade e equidade:** creo firmemente que o acceso ás ferramentas de xestión de medicamentos non debe estar limitado por barreiras financeiras. Ao ofrecer a aplicación de xeito gratuíto, garante que todos os que necesiten axuda para xestionar o seu medicamento poidan acceder a ela sen preocuparse polos custos asociados.

**Impacto Social:** O obxectivo principal é mellorar a saúde e o benestar das persoas garantindo un mellor cumprimento dos réximes de medicación. Ao eliminar calquera barreira financeira para acceder á aplicación, espérase que chegue a un público máis amplo e teña un maior impacto na saúde pública.

## 2. Requirimentos
Para o desenvolvemento deste proxecto empregaranse tecnoloxías de desenvolvemento de aplicacións móbiles para Android en concreto Android Studio e a linguaxe de programación Kotlin. Ademais, será necesario o uso de bases de datos locais para almacenar información de usuarios e medicamentos utilizando Room. Tamén serán necesarios os servizos da API REST pública do [CIMA](https://cima.aemps.es/cima/publico/home.htm) (Centro de Información online de Medicamentos) da AEMPS (Axencia Española de Medicamentos e Produtos Sanitarios) para a busqueda dos datos dos medicamentos, tales como dosis, excipientes e acceder ao prospecto.