# mellon
A super simple password "manager", which stores absolutely nothing and only runs in your browser. 

You can even download the code and run it completely offline (you may be missing some fonts, but the functionality will be fine).

## Deployment

~~This code is currently deployed at [mellon.lkroll.com](https://mellon.lkroll.com).~~ No longer deployed. If you still need it feel free to self-host or run locally.

The most recent release is [2.0.0](https://github.com/Bathtor/mellon/releases/tag/v2.0.0).

## Usage

The idea of mellon is that you pick yourself a parametrised passphrase for all websites you use and which you can remember. The parameters you choose you adjust for every individual website. You can pick whatever parameters you need, but we recommend to include at least a version number (in case you needed to reset your password at some point) and something that is unique for the website, such as its name or root url, for example.

For example, I could say that my parametrised passphrase is `Every <version> idiot can use <website> easily.` where `<version>` is where you insert the current version number and `<website>` is where you place the name of the website you are generating a password for.

Now you have a consistent way of creating a long password for every website, by using the passphrase with unique parameters. Of course, you **never ever** write down your passphrase or tell it to **anyone**! It is only in your head, so pick something you can remember. Make it rhyme, if that helps you remember it.

Now, if we could trust website to handle our passwords safely you would be set at this point by just using your parametrised passphrase everywhere. Sadly, as history has shown, we certainly can not do that. And if anyone finds out our passphrase for one website, they can easily deduce the one for every other website, by just trying through a few version numbers and concrete spellings for the website name.

This is where **mellon** comes in. It simply allows you hash your passphrase before you give it to a website, so that if the website leaks your password, all someone gets is the hash of a single parametrised instance of your passphrase. That makes it fairly difficult to recover the actual passphrase.

Mellon 2.x adds some additional features to this basic functionality: Since many website have absolutely silly requirements about characters that can and can't, or must be used in their passwords, mellon 2.x allows you to alter the alphabet the passphrase is encoded in. The default extended alphabet contains something like 90 different characters, while the alternative minimal alphabet only contains basic ASCII characters and numbers and very few special symbols. If that is still not sufficient to fulfill a website's silly requirements, you can *exclude* specific characters (e.g., write "a?!" to exclude "a", "?", and "!"), and you can also *require* specific character, which will preprend them to the output, if they don't already occur within it.

Additionally, you can generate a random salt to add to your passphrase automatically, which essentially functions like an additional unpredictable parameter and extra padding, and makes recovery of the passphrase itself even more difficult.

As remembering all this silly information for every single website is pretty much impossible, mellon 2.x allows you to export/import the settings together with the name/url of the website and the current version id as as a JSON formatted string. Where you store that information is up to you. It is not terribly security relevant, as it is fairly useless without knowledge of the passphrase, but it doesn't hurt to keep reasonably safe either.

## Tracking
In order to get an idea of the usage of website, we use [Simple Analytics](https://simpleanalytics.com/). It is a very transparent traffic analyser, that respects browser's "do not track" flag. If that is still a dealbreaker for you, simply prohibit the loading of anything from `https://sa.lkroll.com` in your browser, and absolutely nothing will be tracked in any way.

## License

Mellon 2.x is licensed under the terms of the MIT license.
