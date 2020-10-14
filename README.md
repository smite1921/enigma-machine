<p align="center"><img width=30% src="https://user-images.githubusercontent.com/43019533/95927179-142ce080-0d8c-11eb-9a91-7d6fc3898085.png"></p>
<h1 align="center">Enigma Machine Simulator</h1>

## About 

<img src="https://upload.wikimedia.org/wikipedia/commons/b/bd/Enigma_%28crittografia%29_-_Museo_scienza_e_tecnologia_Milano.jpg" align="right" width=20%/>

The Enigma Machine is a physical cryptographic device that was used extensively by the German military during World War II. To use it, both parties must agree upon the settings of the machine in advance. To create an encrypted message, one of the parties must input the original message into the Enigma using the keyboard and write down the encoded message through the letters that light up. To decrypt the message, the other party must enter the encoded message through the keyboard and write the original message through the letters that light up. The Enigma machine has  158,962,555,217,826,360,000 different possible settings, this it makes it safe against many code-breaking techniques, and brute force methods.

## Download
**Required:** Andriod device with atleast Lollipop (5.0)

**Play Store Link:** https://play.google.com/store/apps/details?id=com.smitpatel.enigmamachine

The app is available for download through Google Play Store. If the Play Store is unavailable, an APK can be generated through Android Studio or Gradle.

## Guide

### Home Screen
<img src="https://user-images.githubusercontent.com/43019533/95944617-9631ff00-0db6-11eb-832c-f75684c3d60e.png" align="right" width=22%/>

The home screen is the primary interface for using the Enigma Machine. It can be separated into three sections: Rotor Positions, Lampboard & Keyboard, and Text Field

#### Rotor Positions (Red)
The start positions for the three rotors can be adjusted through the three dials at the top of the home screen. The start position for each rotor can be adjusted either by clicking the arrows or by flicking vertically across the dial. 

#### Lampboard & Keyboard (Green)
This is the main section of the home screen. Text can be inputted through the keyboard and the encoded letter can be seen in the lampboard. The app adds a space bar and delete key which was not available in the original Enigma Machine.

#### Text (Blue)
The inputs of the keyboard and outputs of the lampboard are shown here. The top text represents keyboard inputs, while the bottom represents coded letters from the lampboard.

### Settings

<img src="https://user-images.githubusercontent.com/43019533/95944618-97632c00-0db6-11eb-8118-4c6769517bce.png" align="right" width=22%/>

The settings page can be accessed by clicking the power switch. Once there, three major components that can be modified: Reflector, Slots & Ring, and the Plugboard. Once new settings are completed, they can be applied by clicking close. If any changes are made there will be a ring sound and text fields on the home screen will be cleared, otherwise nothing will happen.

#### Reflector (Red)
User can choose which reflector they wish to use from the three options.

#### Slots & Ring (Green)
The first row in this section allows selection for which rotors should be in which slot. From left to right, the first drop down menu selects which rotor should be in the first slot, the second drop down menu selects which rotor should be in slot two, etc. The second row in this section is to set the ring settings for the rotors. Like before , the first drop down menu selects the ring setting for rotor in slot one, the second drop down menu selects the ring setting for the rotor in slot two, etc.

#### Plugboard (Blue)
To make a plugboard pair the user must select two letters. Each pair will have a distinct color. To remove a pair, the user must click on one of the letters in the pair.

## Acknowledgements

### Design 
The central theme and color palette was based on the design for the Enigma C machine from **Cryptomusuem**.
  - **Link:** https://www.cryptomuseum.com/crypto/enigma/c/index.htm 

### Technical Specifications
All technical specifications for the Enigma Machine including rotor mappings, notch positions, etc. were found from **Wikipedia**
  - **Link:** https://en.wikipedia.org/wiki/Enigma_rotor_details 

### Sound
All sound effects used in the application are from **Zapslapt**.
  - **Link:** https://www.zapsplat.com/

### Misc. 
The underlying backend for the rotor dials is from **Shawn Lin's NumberPicker View**
  - **Link:** https://github.com/ShawnLin013/NumberPicker

## License 
```
The MIT License (MIT)
Copyright © 2020 Smit Patel - Enigma Machine Simulator

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```

## Screenshots
<img src="https://user-images.githubusercontent.com/43019533/95921859-138e4d00-0d80-11eb-9945-2c5b98d126c2.png" align="left" width=30%/>
<img src="https://user-images.githubusercontent.com/43019533/95921866-14bf7a00-0d80-11eb-8540-a447a6c17579.png" align="left" width=30%/> <br />



