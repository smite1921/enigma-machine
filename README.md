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

## Instructions

### Key Settings

<img src="https://user-images.githubusercontent.com/43019533/103398419-ad0d0580-4b0a-11eb-937f-4d7e03b1b7dc.png" align="right" width=35%/>

Before any encoding or decoding process, the most important thing is to note down the current settings of the machine. There are essentially five different settings we need to know for any encoding and decoding process. These five settings are the rotor slots, reflector option, ring options, plugboard pairings, and the rotor start positions. Here is an example of noting down the settings:

Suppose the images on the right represent the current settings of the enigma machine. Noting down the settings from that would yield:
  - Rotor slot settings: I II III (Blue Box)
  - Reflector Option: UKW-B (Red Box)
  - Ring Option: A A A (Green Box)
  - Plugboard Pairings: QW EY RF DX GV ZU BN JO MK IL (Brown Box)
  - Rotor Start Positions: A B W (Yellow Box)

### Encoding

<img src="https://user-images.githubusercontent.com/43019533/103398418-ac746f00-4b0a-11eb-8fa6-9a59f191d973.png" align="right" width=23%/>

The first step before any encoding process is to choose the settings for the machine. After choosing the settings we desire and noting them down it is time for the encoding process. To encode a message, simply type the message into the keyboard. For each letter entered in the keyboard, a corresponding letter in the lamp-board will light up. The lit-up letter represents the encoded letter for the letter entered. The full encoded message can be obtained by keeping track of the letters that light up (This was how it was done in WW2) or by looking at the text-boxes at the bottom. The top text-box will have the raw message, and the bottom text-box will have the encoded message. Here is an example of encoding a message:

Suppose the settings we chose for the machine are:
  - Rotor slot settings: II I III 
  - Reflector Option: UKW-B 
  - Ring Option: D B C 
  - Plugboard Pairings: QW EY RF DX GV ZU
  - Rotor Start Positions: A B S

and the message we wish to encode is "THIS IS A SECRET MESSAGE." After typing the message in the keyboard we can see the encoded message in bottom text-box(red box).
### Decoding 

<img src="https://user-images.githubusercontent.com/43019533/103398414-aaaaab80-4b0a-11eb-8e4c-2faaa67c0d96.png" align="right" width=23%/>

The decoding process is just the inverse of the encoding process. To decode, the machine is first set to the settings that were used to encode the message. After that, the encoded message is entered into the keyboard. For each letter entered in the keyboard, a letter in the lamp-board will light up. The lit-up letter represents the decoded letter for the encoded letter entered. The full decoded message is obtained by noting down the letters that light up or by checking the text-boxes at the bottom. The top text-box will have the encoded message, and the bottom text-box will have the decoded message. Here is an example of decoding the message we made in the encoding section:

The settings we chose for the machine when encoded the message were:
  - Rotor slot settings: II I III 
  - Reflector Option: UKW-B 
  - Ring Option: D B C 
  - Plugboard Pairings: QW EY RF DX GV ZU
  - Rotor Start Positions: A B S
 
 After setting the machine to these settings we type the encoded message which was "WAWP WH F HUMWBO CTXNCCX." After typing the message in the keyboard we can see the original message in bottom text-box(greeen box).

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
<img src="https://user-images.githubusercontent.com/43019533/95949688-3856e480-0dc1-11eb-9aa4-d583f46ba711.png" algin="left" width=60%/>

<img src="https://user-images.githubusercontent.com/43019533/95948979-b5815a00-0dbf-11eb-9d4b-6447b7d89340.png" align="center" width=60%/>

