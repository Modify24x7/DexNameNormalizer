# DexNameNormalizer
Normalize obfuscate name

### Usage
    java -jar DexNameNormalizer.jar -inDex claases.dex -outDex classes-Normal.dex -api 15 -outMappingPath classes-Mapping.txt
	
### Before and After

The code on the left is a decompilation of an obfuscated app, and the code on the right has been deobfuscated.
<section>
<p align="center">
<img src="https://raw.githubusercontent.com/Modify24x7/DexNameNormalizer/master/image/ClassName.jpg" alt="" height="380px" align="center" />
</p>
</section>
	
### Note:
    App not runnable after normalize, analyze use only.
	
### Support
    Class rename(read source name and replace)
	
### License
    dexlib2: The 3-Clause BSD License
    guava: http://www.apache.org/licenses/LICENSE-2.0.txt
    jsr350: http://www.apache.org/licenses/LICENSE-2.0.txt
