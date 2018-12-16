package net.haesleinhuepf.imagej.macro.modules;

import clearcl.ClearCLBuffer;
import clearcl.ClearCLImage;
import net.haesleinhuepf.imagej.kernels.Kernels;
import net.haesleinhuepf.imagej.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.imagej.macro.CLIJMacroPlugin;
import net.haesleinhuepf.imagej.macro.CLIJOpenCLProcessor;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_mean3d")
public class Mean3D extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor {

    @Override
    public boolean executeCL() {
        int kernelSizeX = radiusToKernelSize(asInteger(args[2]));
        int kernelSizeY = radiusToKernelSize(asInteger(args[3]));
        int kernelSizeZ = radiusToKernelSize(asInteger(args[4]));

        if (containsCLImageArguments()) {
            return Kernels.mean(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]), kernelSizeX, kernelSizeY, kernelSizeZ);
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = Kernels.mean(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), kernelSizeX, kernelSizeY, kernelSizeZ);
            releaseBuffers(args);
            return result;
        }
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ";
    }
}
