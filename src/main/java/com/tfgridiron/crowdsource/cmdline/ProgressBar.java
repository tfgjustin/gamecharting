/*
 * Copyright (c) 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tfgridiron.crowdsource.cmdline;

/**
 * @author justin@google.com (Your Name Here)
 * 
 */
public class ProgressBar {
  private static final int BAR_WIDTH = 60;
  private final String tag;
  private final Integer maxWidth;

  public ProgressBar(String tag, int maxWidth) {
    if (tag == null) {
      throw new IllegalArgumentException("Missing progress bar tag.");
    }
    if (maxWidth <= 0) {
      throw new IllegalArgumentException("Invalid max width: " + maxWidth);
    }
    this.tag = tag;
    this.maxWidth = maxWidth;
  }

  public void printProgress(Integer progress) {
    Float pctDone = progress.floatValue();
    pctDone /= maxWidth;
    if (pctDone > 100f) {
      pctDone = 1.0f;
    }
    Float numToPrint = pctDone * BAR_WIDTH;
    System.out.print("\r" + tag + " [");
    for (int i = 0; i < BAR_WIDTH; ++i) {
      if (i < numToPrint.intValue()) {
        System.out.print("=");
      } else if (i == numToPrint.intValue()) {
        System.out.print(">");
      } else {
        System.out.print(" ");
      }
    }
    System.out.print("]");
  }

  public void finish() {
    System.out.println();
  }

}
