patternswithwildcards
=====================
<h1>Patterns with wildcards</h1>
<p>This is a program which finds all occurrences of a specified string pattern in the input text.
The pattern may contain a number of wildcards.</p> 
<p>For example, searching for <em>'tgt(2)tct(1-6)cat'</em> 
finds all positions in input text 
where <em>'tgt'</em> is followed by arbitrarily two characters (precisely 2), then by <em>'tct'</em>, then by 
unspecified 1-6 characters, and finally by <em>'cat'</em>.</p>
<p>The program is designed to handle arbitrarily large input texts (whole genomes, for example) 
in a small constant amount of RAM.
It reads input line-by-line, never loading the entire input into main memory. 
Each input line is streamed though an in-memory automata, constructed from the specified pattern.</p> 
<p>For example, you can find all occurrences of Zinc finger transcription factor 
'tgt(6)tgt(36-42)cat(6)cat' in the entire human genome.</p>
<p>
Program outputs positions where the pattern occurs and - optionally - the context: 
substrings surrounding the pattern.</p>

<p>Program works with any sequential data, not only DNA strings.</p>

<p>Program includes a simple self-explanatory GUI: to specify program parameters.</p>

<h2>To compile:</h2>
<pre><code>
set path=<em>path to JDK</em>/bin
javac -cp . simplegui/StreamWildcards.java -d ../bin
</code></pre>

<h2>To run:</h2>
<pre><code>
java -Xmx512M -Xms512m simplegui.StreamWildcards
</code></pre>
or use the jar-packaged app:
<pre><code>java -jar STREAMWILDCARDS.jar</code></pre>
<h2>Sample usage:</h2>
There is a sample text file 'Sample.txt'.
Select it through GUI and enter the following pattern to serach: 'the(1-5)re'.
Also specify to append 10 characters from each side. <br>
The output could be written to 'result.txt' specified by the user.<br>
The result file contains tab-delimited lines. Each line consists of: <br>
input text file name, <br>
start position of pattern occurrence, <br>
start position of the context, <br>
and the context string itself: pattern occurrence in the text, surrounded by specified margins. 

