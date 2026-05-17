package fr.bts.iris.slam.controller;

import fr.bts.iris.slam.dao.LayerDAO;
import fr.bts.iris.slam.model.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static fr.bts.iris.slam.Main.navTo;

public class EditorController extends Controller {

    @FXML private VBox editorZone;
    @FXML private VBox editorTop;
    @FXML private HBox editorBottom;
    // @FXML private VBox editorRight;
    @FXML private VBox layersSection;

    @FXML private Label feedbackLabel;

    @FXML private VBox pen;
    @FXML private SVGPath penSVG;
    @FXML private VBox eraser;
    @FXML private SVGPath eraserSVG;

    private double currentLineWidth;
    private Color currentLineColor;

    private Project project;
    private static ArrayList<Layer> layers;
    private static ArrayList<Canvas> canvas;
    private static ArrayList<RadioButton> layerRadioButtons;
    private static LayerDAO layerDAO;

    private Image challengeProjectImage;

    private int currentLayerId;

    private ScrollPane scrollPane;
    private StackPane stackPane;

    private int canvasSizeX;
    private int canvasSizeY;

    private String userProjectsDir;
    private static final String CHALLENGES_DIR = "Challenges/";

    private boolean isConfigurationMenuPresent;

    @FXML
    public void initialize(){
        canvas = new ArrayList<>();
        layerRadioButtons = new ArrayList<>();
        currentLayerId = 0;
        currentLineWidth = 8;
        currentLineColor = Color.BLACK;

        scrollPane = new ScrollPane();
        stackPane = new StackPane();

        try {
            layerDAO = new LayerDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Slider scaleSlider = new Slider();

        scaleSlider.setId("scaleSlider");
        scaleSlider.setMin(0.25); scaleSlider.setMax(1); scaleSlider.setValue(0.25);
        scaleSlider.setMajorTickUnit(0.1); scaleSlider.setShowTickMarks(true);
        scaleSlider.setOnMouseDragged(mouseEvent -> this.scale(scaleSlider.getValue()));

        this.editorBottom.getChildren().add(scaleSlider);
    }

    @Override
    public void setProject(String name, Project value) throws IOException {
        if (name.equals("exist") || name.equals("new")) {
            this.project = value;
            this.setup(name);
        }
    }

    @Override
    public void setString(String name, String value){
        if (name.equals("userProjectsDir")) {
            userProjectsDir = value;
        }
    }

    protected void setup(String mode) throws IOException {
        String path = CHALLENGES_DIR + project.getChallenge_id() + ".png";
        this.challengeProjectImage = new Image(new File(path).toURI().toString());

        scrollPane.setContent(stackPane);
        scrollPane.setContent(stackPane);

        scrollPane.setPannable(false); // Must press ctrl key to pannable ↓
        scrollPane.sceneProperty().addListener((obsValue, oldScene, newScene) -> {
            if (newScene == null) {
                return;
            }
            newScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> scrollPane.setPannable(event.isControlDown()));
            newScene.addEventFilter(KeyEvent.KEY_RELEASED, event -> scrollPane.setPannable(event.isControlDown()));
        });

        this.editorZone.getChildren().add(scrollPane);

        this.canvasSizeX = (int) this.challengeProjectImage.getWidth();
        this.canvasSizeY = (int) this.challengeProjectImage.getHeight();

        new File(userProjectsDir).mkdirs();

        if (Objects.equals(mode, "exist")) {
            layers = layerDAO.getByProjectId(project.getId());
            // clear
            for (int i = 0; i < layers.size(); i++) {
                Path filePath = Path.of(userProjectsDir + "Project " + project.getId() + "/" + layers.get(i).getId() + ".png");
                if (!Files.exists(filePath)) {
                    layerDAO.deleteById(i);
                }
            }
            layers = layerDAO.getByProjectId(project.getId());
            // load
            for (int i = 0; i < layers.size(); i++) {
                File file = new File(userProjectsDir + "Project " + project.getId() + "/" + layers.get(i).getId() + ".png");
                Image image = new Image(file.toURI().toString());

                canvas.add(new Canvas(canvasSizeX, canvasSizeY));
                Canvas canva = canvas.get(i);
                canva.setScaleX(0.25);
                canva.setScaleY(0.25);

                GraphicsContext graphicsContext = canva.getGraphicsContext2D();
                graphicsContext.drawImage(image, 0, 0);

                stackPane.getChildren().add(canva);
            }
            loadLayers();
        }

        if (Objects.equals(mode, "new")) {
            // create
            for (int i = 0; i < 2; i++) {
                String name = "null";
                if (i == 0) {
                    name = "Challenge Image";
                }
                if (i == 1) {
                    name = "Layer";
                }
                Layer layer = new Layer(name, project.getId());
                layerDAO.save(layer, project.getId());
                if (i == 0) {
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(challengeProjectImage, null);
                    File file = new File(userProjectsDir + "Project " + project.getId() + "/" + layer.getId() + ".png");
                    ImageIO.write(bufferedImage, "png", file);
                    FileInputStream inputStream = new FileInputStream(file.getPath());
                    Image image = new Image(inputStream);

                    canvas.add(new Canvas(canvasSizeX, canvasSizeY));
                    Canvas canva = canvas.get(i);
                    canva.setScaleX(0.25);
                    canva.setScaleY(0.25);

                    stackPane.getChildren().add(canva);

                    GraphicsContext graphicsContext = canva.getGraphicsContext2D();
                    graphicsContext.drawImage(image, 0, 0);

                }
                if (i > 0) {
                    BufferedImage bufferedImage = new BufferedImage(canvasSizeX, canvasSizeY, BufferedImage.TYPE_INT_ARGB);
                    File file = new File(userProjectsDir + "Project " + project.getId() + "/" + layer.getId() + ".png");
                    ImageIO.write(bufferedImage, "png", file);
                    FileInputStream inputStream = new FileInputStream(file.getPath());
                    Image image = new Image(inputStream);

                    canvas.add(new Canvas(canvasSizeX, canvasSizeY));
                    Canvas canva = canvas.get(i);
                    canva.setScaleX(0.25);
                    canva.setScaleY(0.25);

                    stackPane.getChildren().add(canva);

                    GraphicsContext graphicsContext = canva.getGraphicsContext2D();
                    graphicsContext.drawImage(image, 0, 0);
                }
            }
            layers = layerDAO.getByProjectId(project.getId());
            save();
            loadLayers();
            pen();
            penSVG.setFill(Color.BLUE);
        }
    }

    private void loadLayers() {
        for (int i = 0; i < layers.size(); i++) {
            Path path = Path.of(userProjectsDir + "Project " + project.getId() + "/" + layers.get(i).getId() +".png");
            if (!Files.exists(path)) {
                layerDAO.deleteById(i);
            }
        }

        for (int i = 0; i < layers.size(); i++) {
            int y = layers.size()-1-i;
            buildJavaFxLayerSection(y, layers.get(y));
        }
        layerRadioButtons.getLast().setSelected(true);
        changeCurrentLayer(0, layerRadioButtons.getLast());
    }

    protected void changeCurrentLayer(int id, RadioButton layerRadioButton) {
        currentLayerId = id;
        for (int i = 0; i < layerRadioButtons.size(); i++) {
            int y = layerRadioButtons.size()-1-i;
            layerRadioButtons.get(y).setSelected(false);
            layerRadioButton.setSelected(true);
        }
    }

    private void updateCanvaPreview() {
        String cssId = "#" + currentLayerId + "LayerPreview";
        HBox monLayerPreview = (HBox) layersSection.lookup(cssId);
        monLayerPreview.getChildren().removeFirst();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        ImageView imageView = new ImageView(canvas.get(currentLayerId).snapshot(parameters, null));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(100);
        monLayerPreview.getChildren().add(imageView);
    }

    protected void resetCurrentLayer() {
        GraphicsContext graphicsContext = canvas.get(currentLayerId).getGraphicsContext2D();
        if (currentLayerId == 0) {
            graphicsContext.drawImage(challengeProjectImage, 0, 0);
        } else {
            graphicsContext.save();
            graphicsContext.clearRect(0, 0, canvasSizeX,canvasSizeY);
            graphicsContext.fill();
            graphicsContext.restore();
        }
    }

    protected void clearCurrentLayer() {
        GraphicsContext graphicsContext = canvas.get(currentLayerId).getGraphicsContext2D();
        if (currentLayerId == 0) {
            graphicsContext.save();
            graphicsContext.setFill(Color.WHITE);
            graphicsContext.fillRect(0, 0, canvasSizeX,canvasSizeY);
        } else {
            graphicsContext.save();
            graphicsContext.clearRect(0, 0, canvasSizeX,canvasSizeY);
        }
        graphicsContext.fill();
        graphicsContext.restore();
    }

    protected void scale(double value) {
        for (Canvas canva : canvas) {
            canva.setScaleX(value);
            canva.setScaleY(value);
        }
    }

    @FXML
    protected void submit() throws IOException {
        save();
        String path = userProjectsDir + "Project " + project.getId() + "/-1.png";

        Controller submitController = navTo(ViewEnum.SUBMIT);
        submitController.setString("userProjectsDir", userProjectsDir);
        submitController.setString("path", path);
        submitController.setProject("default", project);
    }

    @FXML
    protected void save() throws IOException {
        for (int i = 0; i < canvas.size(); i++) {
            Canvas canva = canvas.get(i);

            double currentScale = Math.max(canvas.getFirst().getScaleX(), canvas.getFirst().getScaleY());
            scale(1);

            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            WritableImage writableImage = canva.snapshot(snapshotParameters, null);

            scale(currentScale);

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            File file = new File(userProjectsDir + "Project " + project.getId() + "/" + layers.get(i).getId() + ".png");
            ImageIO.write(bufferedImage, "png", file);
        }

        Canvas fusion = new Canvas(canvasSizeX, canvasSizeY);
        GraphicsContext fusionGraphicsContext = fusion.getGraphicsContext2D();

        double currentScale = Math.max(canvas.getFirst().getScaleX(), canvas.getFirst().getScaleY());
        scale(1);
        for (Canvas canva : canvas) {
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            WritableImage writableImage = canva.snapshot(snapshotParameters, null);
            fusionGraphicsContext.drawImage(writableImage, 0, 0);
        }
        scale(currentScale);

        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        WritableImage writableImage = fusion.snapshot(snapshotParameters, null);

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        String path = userProjectsDir + "Project " + project.getId() + "/-1.png";
        File file = new File(path);
        ImageIO.write(bufferedImage, "png", file);
    }

    @FXML
    protected void exit() throws IOException {
        navTo(ViewEnum.HOME);
    }

    // Pen and eraser ↓
    @FXML private void pen() {
        if (currentLayerId == 0) {
            feedbackLabel.setText("Le calque Challenge n'est soumis qu'aux Effets.");
            return;
        } else {
            feedbackLabel.setText("");
        }

        if (isConfigurationMenuPresent) {destroyLastSection();}
        buildDrawSection();
        penSVG.setFill(Color.BLACK);
        penSVG.setStroke(Color.TRANSPARENT);

        eraserSVG.setFill(Color.TRANSPARENT);
        eraserSVG.setStroke(Color.BLACK);

        Canvas canva = canvas.get(currentLayerId);
        GraphicsContext graphicsContext = canva.getGraphicsContext2D();
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        graphicsContext.setLineJoin(StrokeLineJoin.ROUND);
        canva.setOnMousePressed(mouseEvent -> {
            if (scrollPane.isPannable()) return;
            graphicsContext.save();
            graphicsContext.setStroke(currentLineColor);
            graphicsContext.setLineWidth(currentLineWidth);

            graphicsContext.beginPath();
            Point2D point2D = canva.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            graphicsContext.moveTo(point2D.getX(), point2D.getY());
            graphicsContext.stroke();

            updateCanvaPreview();
        });
        canva.setOnMouseDragged(mouseEvent -> {
            if (scrollPane.isPannable()) return;
            Point2D point2D = canva.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            graphicsContext.lineTo(point2D.getX(), point2D.getY());
            graphicsContext.stroke();

            updateCanvaPreview();
        });
        canva.setOnMouseReleased(mouseEvent -> graphicsContext.restore());
    }
    @FXML private void eraser() {
        if (currentLayerId == 0) {
            feedbackLabel.setText("Le calque Challenge n'est soumis qu'aux Effets.");
            return;
        } else {
            feedbackLabel.setText("");
        }

        if (isConfigurationMenuPresent) {destroyLastSection();}
        buildEraserSection();
        eraserSVG.setFill(Color.BLACK);
        eraserSVG.setStroke(Color.TRANSPARENT);

        penSVG.setFill(Color.TRANSPARENT);
        penSVG.setStroke(Color.BLACK);

        Canvas canva = canvas.get(currentLayerId);
        GraphicsContext graphicsContext = canva.getGraphicsContext2D();
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        graphicsContext.setLineJoin(StrokeLineJoin.ROUND);
        canva.setOnMousePressed(mouseEvent -> {
            if (scrollPane.isPannable()) return;
            graphicsContext.save();

            Point2D point2D = canva.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            graphicsContext.clearRect(point2D.getX()-currentLineWidth/2, point2D.getY()-currentLineWidth/2, currentLineWidth, currentLineWidth);

            updateCanvaPreview();
        });
        canva.setOnMouseDragged(mouseEvent -> {
            if (scrollPane.isPannable()) return;

            Point2D point2D = canva.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            graphicsContext.clearRect(point2D.getX()-currentLineWidth/2, point2D.getY()-currentLineWidth/2, currentLineWidth, currentLineWidth);

            updateCanvaPreview();
        });
        canva.setOnMouseReleased(mouseEvent -> graphicsContext.restore());
    }

    // Filter and transformation ↓
    @FXML
    private void selectFiler(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        String menuItemId = menuItem.getId();

        double currentScale = Math.max(canvas.getFirst().getScaleX(), canvas.getFirst().getScaleY());
        scale(1);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage writableImage = canvas.get(currentLayerId).snapshot(parameters, null);
        scale(currentScale);

        if (isConfigurationMenuPresent) {destroyLastSection();}
        
        if (Objects.equals(menuItemId, "grayColors")) {
            clearCurrentLayer();
            filter(writableImage, FilterEnum.GRAY);
            updateCanvaPreview();
        }
        if (Objects.equals(menuItemId, "inverseColors")) {
            clearCurrentLayer();
            filter(writableImage, FilterEnum.INVERSE);
            updateCanvaPreview();
        }
        if (Objects.equals(menuItemId, "rotate")) {
            buildRotationSection(writableImage);
        }
    }

    protected void filter(WritableImage source, FilterEnum filter) {
        WritableImage destination = new WritableImage(this.canvasSizeX, this.canvasSizeY);
        PixelReader reader = source.getPixelReader();
        PixelWriter writer = destination.getPixelWriter();

        for (int x = 0; x < this.canvasSizeX; x++) {
            for (int y = 0; y < this.canvasSizeY; y++) {
                int sourceColor = reader.getArgb(x, y);
                if (filter == FilterEnum.GRAY) {
                    int destinationColor = grayFilter(sourceColor);
                    writer.setArgb(x, y, destinationColor);
                }
                if (filter == FilterEnum.INVERSE) {
                    int destinationColor = inverseFilter(sourceColor);
                    writer.setArgb(x, y, destinationColor);
                }
            }
        }
        canvas.get(currentLayerId).getGraphicsContext2D().getPixelWriter().setPixels(0, 0, this.canvasSizeX, this.canvasSizeY, destination.getPixelReader(), 0,0);
    }
    protected int inverseFilter(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        if (alpha == 0) {return argb;}

        int red = (argb >> 16) & 0xFF; int green = (argb >> 8) & 0xFF; int blue = argb & 0xFF;
        int finalRed = (int) (255.0 - red);
        int finalGreen = (int) (255.0 - green);
        int finalBlue = (int) (255.0 - blue);

        return (alpha << 24) | (finalRed << 16) | (finalGreen << 8) | finalBlue;
    }
    protected int grayFilter(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        if (alpha == 0) {return argb;}

        int red = (argb >> 16) & 0xFF; int green = (argb >> 8) & 0xFF; int blue = argb & 0xFF;
        int lum = (int) (red * 0.2126 + green * 0.7152 + blue * 0.0722);

        return (alpha << 24) | (lum << 16) | (lum << 8) | lum;
    }

    protected void rotation(Image source, GraphicsContext graphicsContext, int angle) {
        int w = (int) source.getWidth();
        int h = (int) source.getHeight();

        graphicsContext.save();
        graphicsContext.translate((double) w/2, (double) h/2);
        graphicsContext.rotate(angle%360);
        graphicsContext.translate(-((double) w/2), -((double) h/2));

        graphicsContext.drawImage(source, 0, 0);

        graphicsContext.restore();
    }

    // JavaFX build functions ↓
    public void buildJavaFxLayerSection(int id, Layer layer) {
        Region regionBetweenHBox = new Region();
        regionBetweenHBox.prefHeight(16);

        HBox hBox = new HBox();
        hBox.setId(project.getId() + "Layer" + "Section");
        Insets insets = new Insets(8.0, 8.0, 8.0, 8.0);
        hBox.setPadding(insets);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-border-color: #000000; -fx-border-style: solid solid solid solid;");
        layersSection.getChildren().add(hBox);
        layersSection.getChildren().add(regionBetweenHBox);

        HBox layerPreview = new HBox();
        layerPreview.setId(id + "LayerPreview");
        layerPreview.setMaxSize(100, 100);
        layerPreview.setMinSize(100, 100);
        layerPreview.setPrefSize(100, 100);

        // updateCanvaPreview()
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        ImageView imageView = new ImageView(canvas.get(id).snapshot(parameters, null));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(100);
        layerPreview.getChildren().add(imageView);

        hBox.getChildren().add(layerPreview);

        Region region = new Region();
        region.setMaxWidth(64);
        region.setMinWidth(64);
        region.setPrefWidth(64);
        hBox.getChildren().add(region);

        VBox layerInteraction = new VBox();
        layerInteraction.setId(project.getId() + "LayerInteraction");
        layerInteraction.setAlignment(Pos.CENTER_RIGHT);
        layerInteraction.setSpacing(8);
        Text text = new Text(layer.getName());
        layerInteraction.getChildren().add(text);
        RadioButton layerRadioButton = new RadioButton();
        layerRadioButton.setOnAction(event -> changeCurrentLayer(id, layerRadioButton));
        layerRadioButton.getStyleClass().add("toggle-button");
        layerRadioButtons.add(layerRadioButton);
        layerInteraction.getChildren().add(layerRadioButton);
        Button layerButton = new Button();
        layerButton.setOnAction(event -> resetCurrentLayer());
        layerButton.setText("Reset");
        layerInteraction.getChildren().add(layerButton);
        hBox.getChildren().add(layerInteraction);
    }

    public void buildRotationSection(WritableImage writableImage) {
        HBox filterConfigurator = new HBox();
        filterConfigurator.setId("filterConfigurator");
        this.editorTop.getChildren().add(filterConfigurator);
        this.isConfigurationMenuPresent = true;

        Slider angleSlider = new Slider();
        Button rotateButton = new Button();

        angleSlider.setId("angleSlider");
        angleSlider.setMin(-360);
        angleSlider.setMax(360);
        angleSlider.setShowTickLabels(true);
        angleSlider.setShowTickMarks(true);
        angleSlider.setMajorTickUnit(45);
        angleSlider.setOnMouseDragged(mouseEvent -> rotateButton.setText(Double.toString((int)angleSlider.getValue())));
        rotateButton.setId("angleButton");
        rotateButton.setText("0.0");
        rotateButton.setOnAction(actionEvent1 -> {
            clearCurrentLayer();
            rotation(writableImage, canvas.get(currentLayerId).getGraphicsContext2D(), (int) angleSlider.getValue());
            updateCanvaPreview();
            destroyLastSection();
        });

        filterConfigurator.getChildren().add(angleSlider);
        filterConfigurator.getChildren().add(rotateButton);
    }

    public void buildDrawSection() {
        HBox filterConfigurator = new HBox();
        filterConfigurator.setId("filterConfigurator");
        this.editorTop.getChildren().add(filterConfigurator);
        this.isConfigurationMenuPresent = true;

        Slider widthSlider = new Slider();
        Text widthValue = new Text();
        ColorPicker colorPicker = new ColorPicker();
        Button button = new Button();
        Region shortRegion = new Region(); shortRegion.setPrefWidth(8);
        Region region = new Region(); region.setPrefWidth(8);
        Region secondRegion = new Region(); secondRegion.setPrefWidth(8);

        widthSlider.setId("widthSlider");
        widthSlider.setMin(0);
        widthSlider.setMax(64);
        widthSlider.setValue(currentLineWidth);
        widthSlider.setShowTickLabels(true);
        widthSlider.setShowTickMarks(true);
        widthSlider.setMajorTickUnit(16);
        widthValue.setText(Double.toString((int)widthSlider.getValue()));
        widthSlider.setOnMouseDragged(mouseEvent -> {
            widthValue.setText(Double.toString((int)widthSlider.getValue()));
            currentLineWidth = widthSlider.getValue();
        });
        widthValue.setId("widthValue");
        widthValue.setText("0.0");
        colorPicker.setValue(currentLineColor);
        colorPicker.setOnAction(actionEvent -> currentLineColor = colorPicker.getValue());
        button.setText("Cacher");
        button.setOnAction(actionEvent -> {
            pen();
            destroyLastSection();
        });
        filterConfigurator.getChildren().add(widthSlider);
        filterConfigurator.getChildren().add(shortRegion);
        filterConfigurator.getChildren().add(widthValue);
        filterConfigurator.getChildren().add(region);
        filterConfigurator.getChildren().add(colorPicker);
        filterConfigurator.getChildren().add(secondRegion);
        filterConfigurator.getChildren().add(button);
    }

    public void buildEraserSection() {
        HBox filterConfigurator = new HBox();
        filterConfigurator.setId("filterConfigurator");
        this.editorTop.getChildren().add(filterConfigurator);
        this.isConfigurationMenuPresent = true;

        Slider widthSlider = new Slider();
        Text widthValue = new Text();
        Button button = new Button();
        Region shortRegion = new Region(); shortRegion.setPrefWidth(8);
        Region region = new Region(); region.setPrefWidth(8);

        widthSlider.setId("widthSlider");
        widthSlider.setMin(0);
        widthSlider.setMax(64);
        widthSlider.setValue(currentLineWidth);
        widthSlider.setShowTickLabels(true);
        widthSlider.setShowTickMarks(true);
        widthSlider.setMajorTickUnit(16);
        widthValue.setText(Double.toString((int)widthSlider.getValue()));
        widthSlider.setOnMouseDragged(mouseEvent -> {
            widthValue.setText(Double.toString((int)widthSlider.getValue()));
            currentLineWidth = widthSlider.getValue();
        });
        widthValue.setId("widthValue");
        widthValue.setText("0.0");
        button.setText("Cacher");
        button.setOnAction(actionEvent -> {
            eraser();
            destroyLastSection();
        });
        filterConfigurator.getChildren().add(widthSlider);
        filterConfigurator.getChildren().add(shortRegion);
        filterConfigurator.getChildren().add(widthValue);
        filterConfigurator.getChildren().add(region);
        filterConfigurator.getChildren().add(button);
    }

    private void destroyLastSection(){
        this.editorTop.getChildren().removeLast();
        this.isConfigurationMenuPresent = false;
    }

}
