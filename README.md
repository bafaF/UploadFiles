## Spring web application : UploadFiles
You can upload files without size limit and it will be avaliable for 1 week or 1 day if file is too big (100Go by default).

# How to use:
Clone this repo, go to the `application.properties` and change it with your info,
you need a [Google client ID and Google client secret](https://support.google.com/workspacemigrate/answer/9222992?hl=fr),
run it with `mvn spring-boot:run` or you IDE.

At the first init you may have warnigs, I'm working on it but if you have a fix feel free to open an issue :).

After the first init edit this `spring.jpa.hibernate.ddl-auto=create` to this `spring.jpa.hibernate.ddl-auto=update` so that your DB don't reset after each restart.

# I am not responsible for the content of the files uploaded and downloaded on the application. So use it at your own risk.
