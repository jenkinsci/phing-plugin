/*
 * The MIT License
 * 
 * Copyright (c) 2008-2011, Jenkins project, Seiji Sogabe
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.phing.console;

import hudson.console.LineTransformationOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Filter {@link OutputStream} that places an annotation that marks Phing target execution.
 *
 * @author Seiji Sogabe
 */
public class PhingConsoleAnnotator extends LineTransformationOutputStream {

    private final OutputStream out;

    private final Charset charset;

    private boolean seenEmptyLine;

    public PhingConsoleAnnotator(OutputStream out, Charset charset) {
        super();
        this.out = out;
        this.charset = charset;
    }

    @Override
    protected void eol(byte[] b, int len) throws IOException {

        String line = charset.decode(ByteBuffer.wrap(b, 0, len)).toString();

        // trim off CR/LF from the end
        line = trimEOL(line);

        if (seenEmptyLine && line.endsWith(":") && line.indexOf('>') > 0) {
            new PhingTargetNote().encodeTo(out);
        }

        if (line.startsWith("Fatal error: ") || line.startsWith("Warning error: ") 
                || line.startsWith("Parse error: ") || line.startsWith("Notice: ")) {
            new PhingPHPErrorNote().encodeTo(out);
        }
        
        if (seenEmptyLine && (line.startsWith("BUILD FINISHED") || line.startsWith("BUILD FAILED"))) {
            new PhingOutcomeNote().encodeTo(out);
        }

        seenEmptyLine = line.length() == 0;
        out.write(b, 0, len);
    }

    @Override
    public void close() throws IOException {
        super.close();
        out.close();
    }
}
