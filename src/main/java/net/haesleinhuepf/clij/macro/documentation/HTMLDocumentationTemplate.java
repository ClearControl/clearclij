package net.haesleinhuepf.clij.macro.documentation;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasLicense;


public class HTMLDocumentationTemplate {
    private String description;
    private String availableForDimensions;
    private String sourceUrl;
    private String headline;
    private String parameterHelpText;
    private String docsUrl;
    private String clijUrl;
    private String license = null;
    private String author = null;

    public HTMLDocumentationTemplate(String description, String availableForDimensions, CLIJMacroPlugin source) {
        this(description, availableForDimensions, source, false);
    }
    public HTMLDocumentationTemplate(String description, String availableForDimensions, CLIJMacroPlugin source, boolean short_license) {
        this.description = description;
        this.availableForDimensions = availableForDimensions;

        this.headline = CLIJUtilities.classToName(source.getClass());
        this.parameterHelpText = source.getParameterHelpText();
        String package_string = source.getClass().getPackage().toString();
        if (package_string.contains(".clij2.") || package_string.contains(".clijx.") )  {
            this.sourceUrl = DocumentationUtilities.clij2rootSourceUrl + source.getClass().getName().replace(".", "/") + ".java";
            this.docsUrl = DocumentationUtilities.clij2docsSourceUrl + "reference_" + headline.replace("CLIJ2_", "").replace("CLIJx_", "");
        } else {
            this.sourceUrl = DocumentationUtilities.rootSourceUrl + source.getClass().getName().replace(".", "/") + ".java";
            this.docsUrl = DocumentationUtilities.docsSourceUrl + "reference#" + headline;
        }
        this.clijUrl = DocumentationUtilities.clijRootUrl;

        if (source instanceof HasAuthor) {
            author = ((HasAuthor) source).getAuthorName();
            if (author != null) {
                this.description =
                        "Author: " + author+ "\n\n" + this.description;
            }
        }
        if (source instanceof HasLicense) {
            license = ((HasLicense) source).getLicense();
            if (short_license) {
                String[] temp = license.split("\n");
                if (temp.length > 5) {
                    license =
                            temp[0] + "\n" +
                                    temp[1] + "\n" +
                                    temp[2] + "\n" +
                                    temp[3] + "\n" +
                                    temp[4] + "...";
                }
            }
            if (license != null){
                this.description = this.description +
                        "License: " + license + "\n\n";

            }
        }
    }

    public String toString() {
        return toString(false);
    }

    public String toString(boolean includeLinks) {
        String output = "";

        if (includeLinks) {
            output = output +
                    "<b><a href=\"" + docsUrl + "\">" + headline + "</a></b><br/><br/>";
        }

        output = output +
                description.replace("\n", "<br/>") + "<br/><br/>" +
                "Parameters: " + parameterHelpText + "<br/>" +
                "Available for: " + availableForDimensions + "<br/><br/>";


        if (includeLinks) {
            output = output +
                    "<a href=\"" + docsUrl + "\">Documentation</a><br/>" +
                    "<a href=\"" + sourceUrl + "\">Source</a><br/>" +
                    "<a href=\"" + clijUrl + "\">clij on the web</a><br/>";
        }

        return output;
    }
}
