package net.haesleinhuepf.clij.macro.modules;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.radiusToKernelSize;

/**
 * Author: @haesleinhuepf
 * December 2018
 */
@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_mean2DSphere")
public class Mean2DSphere extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        int kernelSizeX = radiusToKernelSize(asInteger(args[2]));
        int kernelSizeY = radiusToKernelSize(asInteger(args[3]));

        if (containsCLImageArguments() && clij.hasImageSupport()) {
            return Kernels.meanSphere(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), kernelSizeX, kernelSizeY);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.meanSphere(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), kernelSizeX, kernelSizeY);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY";
    }

    @Override
    public String getDescription() {
        return "Computes the local mean average of a pixels ellipsoidal neighborhood. The ellipses size is specified by \n" +
                "its half-width and half-height (radius)." +
                "\n\nDEPRECATED: This method is deprecated. Use CLIJ2 instead.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D";
    }

}
