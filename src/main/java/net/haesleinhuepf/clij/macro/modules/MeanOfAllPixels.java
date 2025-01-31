package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_meanOfAllPixels")
public class MeanOfAllPixels extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        double sum = 0;
        double numberOfPixels = 0;
        if (containsCLImageArguments() && clij.hasImageSupport()) {
            ClearCLImage image = (ClearCLImage)( args[0]);
            sum = Kernels.sumPixels(clij, image);
            numberOfPixels = image.getWidth() * image.getHeight() * image.getDepth();
        } else {
            Object[] args = openCLBufferArgs();
            ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);
            sum = Kernels.sumPixels(clij, buffer);
            numberOfPixels = buffer.getWidth() * buffer.getHeight() * buffer.getDepth();
            releaseBuffers(args);
        }

        ResultsTable table = ResultsTable.getResultsTable();
        table.incrementCounter();
        table.addValue("Mean", sum / numberOfPixels);
        table.show("Results");
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source";
    }

    @Override
    public String getDescription() {
        return "Determines the mean average of all pixels in a given image. It will be stored in a new row of ImageJs\n" +
                "Results table in the column 'Mean'." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
