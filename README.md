# FrameLab_desktop

## Descriptif

Bienvenue sur le dépôt Git de FrameLab Desktop !

Vous pouvez trouver ici la présentation du projet, ainsi que son guide de lancement.

## Présentation du projet

FrameLab Desktop est un programme, mais aussi et surtout, un éditeur d'images.

Connectez-vous, ou utilisez le mode démo, pour accéder à l'accueil, où vous retrouverez le challenge actuel ainsi que vos projets. Créez ou chargez un projet, et éditez-le dans l'éditeur, ensuite, il vous est possible d'enregistrer celui-ci, et de le soumettre comme participation au site web (sauf en mode démo).

## Guide de lancement

### Téléchargement de Java 21

Vérifiez si vous avez déjà Java, tapez dans le terminal de votre choix pour Linux comme pour Mac ou votre CMD pour Windows (seulement CMD, ce tutoriel ne fait pas usage de PowerShell) ceci :

```
java -version
```

Si vous avez `openjdk version 21.XX.XX` (recommandé), ou `openjdk version 25.XX.XX`, en sortie, vous êtes prêt à passer à la suite.

Dans le cas contraire, veuillez retrouver des exécutables chez [Adoptium](https://adoptium.net/fr/temurin/releases?version=21&os=any&arch=any), ils fournissent des exécutables OpenJDK précompilés et gratuits, étant donné qu'Oracle vous demandera un compte et une entreprise.

Sélectionnez le JRE, le JDK nous est inutile car il ne concerne que le développement, et téléchargez-le en cliquant sur l'icône à côté de TAR.GZ pour Linux et Mac, ou ZIP pour Windows, et avec les moyens de votre choix, extrayez-en le contenu. Il vous faut à la fin un dossier qui contienne au moins le dossier bin ayant les exécutables Java, copiez le chemin absolu vers ce Java.

Maintenant, pour utiliser Java, tapez le chemin absolu que vous avez copié, et vérifiez la version avec les exemples disponibles ci-dessous :<br>
  Pour Linux et Mac :
```bash
"~/Downloads/jdk-21.0.11+10-jre/bin/java" -version
```
  Pour Windows :
```cmd
"C:\Users\%USERNAME%\Downloads\jdk-21.0.11+10-jre\bin\java" -version
```
Vous devez maintenant avoir `openjdk version 21.XX.XX` en sortie, vous êtes prêt à passer à la suite.

> [!NOTE]
> Sachez que vous n'avez pas installé Java, ce que cela veut dire est que si vous n'en voulez plus, il vous suffit simplement de supprimer le dossier téléchargé.

### Téléchargement du Fat JAR

Dans les [sorties](https://github.com/AntoineLaLune/FrameLab_desktop/releases) / [releases](https://github.com/AntoineLaLune/FrameLab_desktop/releases), deux versions de FrameLab vous sont proposées, si vous êtes sous Linux, Windows ou Mac Silicon, téléchargez la version `FrameLab-v1.X.X.jar`, et si vous êtes sous Mac Intel, téléchargez la version `FrameLab-v1.X.X_macOS_Intel.jar`.

> [!NOTE]
> Sachez que le programme crée des dossiers au même niveau que celui-ci, il est donc recommandé de le placer dans un dossier vide qui lui est réservé.

### Lancement du Fat JAR

Enfin, pour lancer le programme, tapez toujours le chemin absolu que vous avez copié, ajoutez l'argument `-jar` signifiant l'exécution d'un JAR, ainsi que le chemin vers le JAR téléchargé (ici le Fat JAR du programme) :
  Pour Linux et Mac :<br>
```bash
"~/Downloads/jdk-21.0.11+10-jre/bin/java" -jar "~/Downloads/FrameLab/FrameLab-v1.X.X.jar"
```
  Pour Windows :
```cmd
"C:\Users\%USERNAME%\Downloads\jdk-21.0.11+10-jre\bin\java" -jar "~/FrameLab/FrameLab-v1.X.X.jar"
```
C'est fini, le programme est lancé.
