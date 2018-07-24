/*
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.perl6.nqp.truffle.nodes.variables;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import org.perl6.nqp.truffle.nodes.NQPNode;

import org.perl6.nqp.dsl.Deserializer;

import org.perl6.nqp.truffle.NQPScope;
import org.perl6.nqp.truffle.FoundLexical;

public class NQPBindStrVariableNode extends FrameLookupNode {
    final private FrameSlot slot;
    @Child private NQPNode valueNode;

    public NQPBindStrVariableNode(FrameSlot slot, int depth, NQPNode valueNode) {
        super(depth);
        this.slot = slot;
        this.valueNode = valueNode;
    }

    @Override
    public String executeStr(VirtualFrame frame) {
        String value = valueNode.executeStr(frame);
        getFrame(frame).setObject(slot, value);
        return value;
    }

    @Deserializer("bind-str-lexical")
    public static NQPBindStrVariableNode bindLexical(NQPScope scope, String name, NQPNode valueNode) {
        FoundLexical foundLexical = scope.findLexical(name);
        return new NQPBindStrVariableNode(foundLexical.getFrameSlot(), foundLexical.getDepth(), valueNode);
    }

    @Deserializer("bind-str-local")
    public static NQPBindStrVariableNode bindLocal(NQPScope scope, String name, NQPNode valueNode) {
        return new NQPBindStrVariableNode(scope.findLocal(name), 0, valueNode);
    }

    @Override 
    public void executeVoid(VirtualFrame frame) {
        executeStr(frame);
    }
}
