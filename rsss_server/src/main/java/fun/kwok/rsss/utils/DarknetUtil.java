package fun.kwok.rsss.utils;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.dnn.*;
import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;
import org.opencv.utils.Converters;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DarknetUtil {
    static Net net;
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String cfgfilePath = null;
        String weightsfilePath = null;
        File cfgfile = FileUtil.createFile("static/yolov3_kwok.cfg");
        File weightsfile = FileUtil.createFile("static/yolov3_kwok_17000.weights");
        cfgfilePath = cfgfile.getPath();
        weightsfilePath = weightsfile.getPath();
        net = Dnn.readNetFromDarknet(cfgfilePath, weightsfilePath);
        if (net.empty())
            System.out.println("Reading Net error");
    }
    static String[] classNames = new String[]{
            "麻婆豆腐", "酸辣土豆丝", "米饭", "花生米", "炒青菜",
            "扬州炒饭", "炒豆芽", "凉拌木耳", "西红柿炒鸡蛋", "可乐鸡翅",
            "宫保鸡丁", "青椒肉丝", "包子"
    };
    public static int[] IdentifyImage(String ImageURI){
        File file = FileUtil.createFile(ImageURI);
        ImageURI = file.getPath();
        Mat im = Imgcodecs.imread(ImageURI, Imgcodecs.IMREAD_COLOR);
        if (im.empty()) {
            System.out.println("Reading Image error");
        }
        Mat frame = im;
        Size frame_size = new Size(416, 416);
        List<Mat> result = new ArrayList<>();
        List<String> outBlobNames = net.getUnconnectedOutLayersNames();
        Mat blob = Dnn.blobFromImage(frame, 0.00392, frame_size, new Scalar(0), true, false); // We feed one frame of video into the network at a time, we have to convert the image to a blob. A blob is a pre-processed image that serves as the input.//
        net.setInput(blob);
        net.forward(result, outBlobNames); //Feed forward the model to get output //
        float confThreshold = 0.1f; //Insert thresholding beyond which the model will detect objects//
        List<Integer> clsIds = new ArrayList<>();
        List<Float> confs = new ArrayList<>();
        List<Rect2d> rects = new ArrayList<>();
        for (int i = 0; i < result.size(); ++i) {
            // each row is a candidate detection, the 1st 4 numbers are
            // [center_x, center_y, width, height], followed by (N-4) class probabilities
            Mat level = result.get(i);
            for (int j = 0; j < level.rows(); ++j) {
                Mat row = level.row(j);
                Mat scores = row.colRange(5, level.cols());
                Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                float confidence = (float) mm.maxVal;
                Point classIdPoint = mm.maxLoc;
                if (confidence > confThreshold) {
                    int centerX = (int) (row.get(0, 0)[0] * frame.cols()); //scaling for drawing the bounding boxes//
                    int centerY = (int) (row.get(0, 1)[0] * frame.rows());
                    int width = (int) (row.get(0, 2)[0] * frame.cols());
                    int height = (int) (row.get(0, 3)[0] * frame.rows());
                    int left = centerX - width / 2;
                    int top = centerY - height / 2;
                    clsIds.add((int) classIdPoint.x);
                    confs.add((float) confidence);
                    rects.add(new Rect2d(left, top, width, height));
                }
            }
        }
        float nmsThresh = 0.5f;
        MatOfFloat confidences=null;
        try {
             confidences   = new MatOfFloat(Converters.vector_float_to_Mat(confs));
        } catch (Exception e) {

            return null;
        }
        Rect2d[] boxesArray = rects.toArray(new Rect2d[0]);
        MatOfRect2d boxes = new MatOfRect2d(boxesArray);
        MatOfInt indices = new MatOfInt();
        Dnn.NMSBoxes(boxes, confidences, confThreshold, nmsThresh, indices); //We draw the bounding boxes for objects here//
        int[] ind = indices.toArray();

        for (int i = 0; i < ind.length; ++i) {
            int idx = ind[i];
            Rect2d box = boxesArray[idx];
            Imgproc.rectangle(frame, box.tl(), box.br(), new Scalar(0, 0, 255), 1);
        }
        //绘制标签
        BufferedImage bufferedImage=Mat2bufferedImage(frame);
        Graphics2D g=bufferedImage.createGraphics();
        DecimalFormat df = new DecimalFormat("#.##");
        int resultInt[]=new int[ind.length];
        String resultStr="";
        for (int i = 0; i <ind.length ; i++) {
            int idx = ind[i];
            Rect2d box = boxesArray[idx];
            resultInt[i]=clsIds.get(idx);
            String label = classNames[clsIds.get(idx)];
            g.setFont(new Font("宋体",Font.PLAIN,20));
            g.setColor(Color.blue);
            if (box.y<20)
                box.y= box.y+20;
            g.drawString(label,(int)box.x,(int)box.y);
        }
        try {
            FileOutputStream outImgStream = new FileOutputStream(ImageURI);
            ImageIO.write(bufferedImage, "png", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultInt;
    }
    private static BufferedImage Mat2bufferedImage(Mat image) {
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".png", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }
}
