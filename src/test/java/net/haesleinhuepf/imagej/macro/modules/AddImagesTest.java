package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.ImageCalculator;
import net.haesleinhuepf.imagej.ClearCLIJ;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddImagesTest {
    @Test
    public void addPixelwise3d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        // do operation with ImageJ
        ImageCalculator ic = new ImageCalculator();
        ImagePlus sumImp = ic.run("Add create stack", testImp1, testImp2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp1, ClearCLImage.class);
        //convert(testImp1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2, ClearCLImage.class);
        //convert(testImp2, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);
        //convert(testImp1, ClearCLImage.class);

        Kernels.addPixelwise(clij, src, src1, dst);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);
        //clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
    }


    @Test
    public void addPixelwise3d_Buffers() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp1 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);
        ImagePlus testImp2 = TestUtilities.getRandomImage(100, 100, 3, 32, 0, 100);

        // do operation with ImageJ
        ImageCalculator ic = new ImageCalculator();
        ImagePlus sumImp = ic.run("Add create stack", testImp1, testImp2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp1, ClearCLBuffer.class);
        //convert(testImp1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2, ClearCLBuffer.class);
        //convert(testImp2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);
        //convert(testImp1, ClearCLBuffer.class);

        Kernels.addPixelwise(clij, src, src1, dst);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);
        //clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
    }

    @Test
    public void addPixelwise2d() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        // do operation with ImageJ
        ImageCalculator ic = new ImageCalculator();
        ImagePlus sumImp = ic.run("Add create", testImp2D1, testImp2D2);

        // do operation with ClearCL
        ClearCLImage src = clij.convert(testImp2D1, ClearCLImage.class);
        //convert(testImp2D1, ClearCLImage.class);
        ClearCLImage src1 = clij.convert(testImp2D2, ClearCLImage.class);
        //convert(testImp2D2, ClearCLImage.class);
        ClearCLImage dst = clij.createCLImage(src);
        //convert(testImp2D1, ClearCLImage.class);

        Kernels.addPixelwise(clij, src, src1, dst);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);
        //clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
    }

    @Test
    public void addPixelwise2d_Buffer() {
        ClearCLIJ clij = ClearCLIJ.getInstance();
        ImagePlus testImp2D1 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);
        ImagePlus testImp2D2 = TestUtilities.getRandomImage(100, 100, 1, 32, 0, 100);

        // do operation with ImageJ
        ImageCalculator ic = new ImageCalculator();
        ImagePlus sumImp = ic.run("Add create", testImp2D1, testImp2D2);

        // do operation with ClearCL
        ClearCLBuffer src = clij.convert(testImp2D1, ClearCLBuffer.class);
        //convert(testImp2D1, ClearCLBuffer.class);
        ClearCLBuffer src1 = clij.convert(testImp2D2, ClearCLBuffer.class);
        //convert(testImp2D2, ClearCLBuffer.class);
        ClearCLBuffer dst = clij.createCLBuffer(src);
        //convert(testImp2D1, ClearCLBuffer.class);

        Kernels.addPixelwise(clij, src, src1, dst);
        ImagePlus sumImpFromCL = clij.convert(dst, ImagePlus.class);
        //clij.convert(dst, ImagePlus.class);

        assertTrue(TestUtilities.compareImages(sumImp, sumImpFromCL));

        src.close();
        src1.close();
        dst.close();
    }

}