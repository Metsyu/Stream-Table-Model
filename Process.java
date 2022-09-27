import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;
import java.util.Scanner;

public class Process
{
    //Main Function
    public static void main(String[] args)
    {
        //Override the acceptable filename extensions
        final FilenameFilter IMAGE_EXT = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png");
            }
        };
		
        //Initialize valid to false
        boolean valid = false;
		
        //Do, while valid is false
        do
        {
            //Obtain user input
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter the folder you would like to access:");
            String folder = scan.nextLine();
			
            //TODO: "YOUR DIRECTORY HERE"
            //This is an example of an acceptable directory to include: "C:/Users/Bob/Pictures/" 
			//Presuming that directory already exists
            File dir = new File("YOUR DIRECTORY HERE" + folder);
			
            //If the directory path is a valid directory
            if(dir.isDirectory())
            {
                System.out.println("Processing now...");
				
                //Process all files that end with the appropriate filename extensions (".png")
                //Also, ensure that the file that was passed in isn't null
                for(File file : Objects.requireNonNull(dir.listFiles(IMAGE_EXT), "File passed in was null!"))
                {
                    //Call the loadImage function
                    BufferedImage image = loadImage(file);
					
                    //Call the convertImage function
                    convertImage(image);
					
                    //Call the writeImage function
                    writeImage(image, file);
                }
				
                //Set valid to true
                valid = true;
            }
            //Else
            else
            {
                System.out.println("Sorry, this directory doesn't exist.");
            }
        }
        while(!valid);
		
        System.out.println("Image processing is complete.");
    }
	
    //convertImage: Converts the passed in image to black and white
    private static void convertImage(BufferedImage image)
    {
        //Loop through all pixels in the x-axis
        for(int x = 0; x < image.getWidth(); x++)
        {
            //Loop through all pixels in the y-axis
            for(int y = 0; y < image.getHeight(); y++)
            {
                //Obtain the ARGB values for the current pixel
                int Pixel = image.getRGB(x, y);
				
                //Obtain the A (Alpha) value
                int A = (Pixel & 0xff000000) >>> 24;
				
                //Obtain the R (Red) value
                int R = (Pixel & 0xff0000) >> 16;
				
                //Obtain the G (Green) value
                int G = (Pixel & 0xff00) >> 8;
				
                //Obtain the B (Blue) value
                int B = Pixel & 0xff;
				
                //If the pixel's RGB values fall within the acceptable "blue" values (part of the river),
                //convert the RGB values to black
                if(R < 60 && G > 80 && B > 80)
                {
                    R = 0;
                    G = 0;
                    B = 0;
                }
                //Else, convert the RGB values to white
                else
                {
                    R = 255;
                    G = 255;
                    B = 255;
                }
				
                //Save the new RGB values in Pixel
                Pixel = (A << 24) | (R << 16) | (G << 8) | B;
				
                //Set the current pixel's RGB values to those stored in Pixel
                image.setRGB(x, y, Pixel);
            }
        }
    }
	
    //loadImage: Load an image with the passed in filename
    private static BufferedImage loadImage(File file)
    {
        //Set image to null before loading
        BufferedImage image = null;
		
        //Try to load the image with the passed in filename
        try
        {
            image = ImageIO.read(file);
        }
        //If load fails, exit with error code 1
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
		
        //Return the loaded image
        return image;
    }
	
    //writeImage: Write/save an image with the passed in filename and image
    private static void writeImage(BufferedImage image, File file)
    {
        //Try to write the image to the specified directory with the passed in parameters
        try
        {
            //TODO: "YOUR DIRECTORY HERE"
            //This is an example of an acceptable directory to include: "C:/Users/Bob/Pictures/Processed/"
			//Presuming that directory already exists
            //This directory is recommended to be different from the directory in line 34
            ImageIO.write(image, "png", new File("YOUR DIRECTORY HERE" + file.getName()));
        }
        //If write fails, exit with error code 1
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}