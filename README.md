# RegDebug plugin

RegDebug extends the existing CLion C/C++ LLDB and GDB debugger by providing information about CPU registers.

Features:
<ul>
    <li>Shows hex values of General Purpose, FLAGS and Floating Point Registers</li>
    <li>Formatting hex values to decimal number</li>
    <li>Pretty printing FLAGS register</li>
    <li>Marking changed registers according to previous debug step</li>
    <li>Opens Memory View according to hex value in focused register</li>
</ul>

## Usage

There is zero configuration. Just launch your application in debug mode and see <code>RegDebug</code> tab
next to existing Debug process tabs: <code>Console</code>, <code>Debugger</code> etc.

## Installation

You can install this plugin from JetBrains plugin repository.
