/*
 * Copyright (C) 2015 Holmes Team at HUAWEI Noah's Ark Lab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.spark.streamdm.streams

import com.github.javacliparser._
import org.apache.spark.streamdm.core._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streamdm.core.Instance

/**
 * Stream reader that gets instances from a file stream
 */
class TextStreamReader extends StreamReader{

  val fileOption: StringOption = new StringOption("file", 'f',
    "Directory where the files are", ".")

  val instanceOption: StringOption = new StringOption("instanceType", 't',
    "Type of the instance to use", "dense")

  def getExamples(ssc:StreamingContext): DStream[Example] = {
    //stream is a localhost socket stream
    val text = ssc.textFileStream(fileOption.getValue)
    //transform stream into stream of instances
    //instances come as whitespace delimited lines, where the first item is the
    //instance of the label(s) and the second is the instance of the features
    text.map(x => Example.parse(x, instanceOption.getValue, "dense"))
  }

  /**
   * Obtains the specification of the examples in the stream
   * @return an specification of the examples
   */
  def getExampleSpecification(): ExampleSpecification = new ExampleSpecification(new InstanceSpecification(), new InstanceSpecification())
}