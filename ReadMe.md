# ClearCLIJ

ClearCLIJ is an ImageJ/Fiji plugin allowing you to run OpenCL code from withing Fijis script editor (e.g. jython). ClearCLIJ is based on [ClearCL](http://github.com/ClearVolume/ClearCL), [FastFuse](https://github.com/ClearControl/FastFuse), [Imglib2](https://github.com/imglib) and [SciJava](https://github.com/SciJava).

Example code:

```
# initialize the GPU context
lCLIJ = ClearCLIJ.getInstance();

# convert ImageJ image to CL images (ready for the GPU)
lInputCLImage = lCLIJ.converter(lImagePlus).getClearCLImage();
lOutputCLImage = lCLIJ.converter(lImagePlus).getClearCLImage(); # copy again to allocate memory for result image

# downsample the image stack using ClearCL / OpenCL
lResultStack = lCLIJ.execute(DownsampleXYbyHalfTask, "kernels/downsampling.cl", "downsample_xy_by_half_nearest", {"src":lInputCLImage, "dst":lOutputCLImage});

# convert the result back to imglib2 and show it
lResultImg = lCLIJ.converter(lResultStack).getRandomAccessibleInterval();
ImageJFunctions.show(lResultImg);
```
Complete jython examples can be found in the src/main/jython directory. More Java example code can be found in the package clearcl.imagej.demo

## OpenCL Kernel calls with CLIJ.execute()
The execute function asks for three or four parameters
```
lCLIJ.execute(<Class>, "filename_open.cl", "kernelfunction", {"src":image, "dst":image, "more":5, "evenmore":image})

lCLIJ.execute("absolute/or/relative/path/filename_open.cl", "kernelfunction", {"src":image, "dst":image, "more":5, "evenmore":image})
```
* An optional class file as an anchor to have a point for where to start
  searching for the program file (second parameter).
* The open.cl program file will be searched for in the same folder where the
  class (first parameter) is defined. In the first example above, this class
  comes with the dependency FastFuse. Alternatively, an absolute path can be
  proveded if there is no class given as first parameter. In case a relative
  path is given, it must be relative from the current dir of Fiji.
* The name of the kernel function defined in the program file
* A dictionary with all the parameters of the kernel function, such as
  "src" and "dst". It is recommended to have at least a "src" and a "dst"
  parameter, because CLIJ derives image data types and global space from
  these parameters.

## Type agnostic OpenCL
As jython is a type-agnostic programming language, ClearCLIJ targets bringing the same convenience to OpenCL as well. However, in order to make the executed OpenCL programs image pixel type agnostic, some conventions must be introduced. The conventions are all optional. OpenCL programmers who know how to pass images of a defined type to OpenCL programs using the correct access functions can skip this section.

* Instead of using functions like `read_imagef()`, `write_imagef()`, `write_imageui()` etc.,
it is recommended to use `WRITE_IMAGE()` and `READ_IMAGE()` function calls. These function
calls will be replaced during runtime with the function accessing the correct image data
type. However, in order to allow ClearCLIJ to detect the right image data type, there must
be at least two image type parameters containing "src", "dst", "input", or "output" in their
parameter names. ClearCLIJ will then for example detect the type of an image parameter called
"src_image" and replace all calls to `READ_IMAGE()` with the respective call to
`image_readui()` or `image_readf()` calls.
* Furthermore, variables inside OpenCL programs can be typed with `DTYPE_IN` and `DTYPE_OUT`
instead of `float` or `int4` in order to make the OpenCL code type agnostic.


## Supported / tested platforms
There is a rudimentary list of tests implemented in the clearcl.imagej.test package mainly testing conversion of types between CPU, GPU and JVM. Furthermore, there is one test applying an OpenCL kernel to images of type UnsignedShort. Following OpenCL devices were tested successfully:

* AMD Radeon RX Vega 3 (OpenCL 2.0, Win 10 64 bit, Sept 2018)
* Nvidia GeForce 940MX (OpenCL 2.0, Win 10 64 bit, Apr 2018)
* NVidia GeForce GTX 960M (OpenCL 1.2, Win 10 64 bit, Feb 2018)
* Intel(R) HD Graphics 620 (OpenCL 2.0, Fedora 27, Apr 2018)
* Intel(R) HD Graphics 620 (OpenCL 1.2, Win 10 64bit, Apr 2018)
* Intel(R) HD Graphics 530 (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) HD Graphics 515 (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) HD Graphics 405 (OpenCL 1.2, Win 10 64 bit, Feb 2018)
* Intel(R) Core(TM) i7-7500U CPU @ 2.70GHz (OpenCL 1.2, Win 10 64 bit, Apr 2018)
* Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) Core(TM) m3-6Y30 CPU @ 0.90GHz (OpenCL 2.0, Win 10 64 bit, Feb 2018)
* Intel(R) Atom(TM) x7-Z8750  CPU @ 1.60GHz (OpenCL 1.2, Win 10 64 bit, Feb 2018)

Tests failed on these devices:

* AMD Ryzen 3 (OpenCL 1.2, Win 10 64 bit, Sept 2018)
* AMD A10-8700P Radeon R6, 10 Compute Cores 4C+6G (OpenCL 1.2, Win 10 64 bit, Feb 2018)
* Intel(R) Core(TM) i7-4980HQ CPU @ 2.80GHz (OpenCL 1.2, macOS 10.12.6, Feb 2018)
* Intel(R) Core(TM) i7-8650U CPU @ 1.90GHz (OpenCL 1.2, Win 10 64 bit, Mar 2018)
* Intel(R) Core(TM) i7-8550U CPU @ 1.80GHz (OpenCL 2.0, Fedora 27, Apr 2018)

## Installation

Clone this repo
```
git clone https://github.com/ClearControl/ClearCLIJ
```

Open pom.xml and enter the path of your Fiji installation in the line containing

```
<imagej.app.directory>C:/path/to/Fiji.app
```

Go to the source dir and deploy to your Fiji.app

```
cd ClearCLIJ
deploy.bat
```

Take care: ClearCLIJ is in early developmental stage. Installing it to your Fiji may harm your Fiji installation as it brings dependencies which may be incompatible with other plugins. It is recommended not to work in a production environment.

# Troubleshooting
* "java.io.IOException: Cannot find source: [Object] <path/filename.cl>" exception: Navigate to the jars subdirectory of your Fiji installation and locate `clearcl.jar` files, e.g. by typing `dir clearcl*` or `ls clearcl*`. If there are several versions installed, remove the older one. In order to fix this exception, you need at least `clearcl-0.5.5-RH.jar`.
* "clearcl.exceptions.ClearCLException: problem while setting argument 'parameter_of_type_float'": To hand over parameters of type float, you need to explicitly type it. Use `from java.lang import Float` and `Float(1.5)` to handover a value of 1.5 to an OpenCL parameter of type float.


