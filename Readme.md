# safeAPIKeyStore [![Build Status](https://travis-ci.org/vatbub/randomusers.svg?branch=master)](https://travis-ci.org/vatbub/randomusers)
Big online service providers often require an api key to use their apis. They tell you to keep this api key secret and to treat it like a password.
But what if you want to use the api in an open source application with anonymous access? This library will help you out!

## The problem
API providers often require the usage of an api key in order to use the api. 
That key obviously should be kept secret. On a server application, that is no problem: 
The key can simply be stored in an environment variable.
But for open source client applications, storing the api key can become a problem. 
Especially applications which target the average consumer cannot request their users to create
their own dropbox application or their own AWS Account to get their own personal api key since 
most of them wont even know what an api is.

On the other hand, API keys should not be thrown on the internet for free access since this 
might impose security issues or might even violate the Terms of Service of the API provider.

## Common solutions (from easy to secure)
### Store the api key in plain text in a string variable.
#### Pros
- Easy to implement
#### Cons
- API key is freely available to everybody
- API key is easy to find in the source code
- Decompilation will reveal the source code
### Using a tool like the [simple string obfuscator](https://github.com/shamanland/simple-string-obfuscator)
#### Pros
- Easy to implement
- API key is harder to find in the source code
- Decompilation-proof
#### Cons
- The API key is still freely available in the source code since everybody can just copy-paste the obfuscated string, compile and print it on the command line.
### Encrypt the API key, save it in the VCS and decrypt it at runtime
#### Pros
- API key is harder to find
- Decompilation-proof
#### Cons
- Attackers can simply copy the decryption code and redirect its output to the console
### Sent all requests to your own server which adds the authentication and forwards the traffic to the API provider
#### Pros
- No API keys will ever be revealed
#### Cons
- You need to run your own server
### Let the user create its own api key
#### Pros
- No API keys will ever be revealed
#### Cons
- To complicated for most consumer applications

## This libraries approach
This library offers a different, unfortunately not entirely fool proof approach to the problem, but it adds layers of security which make it harder for attackers to find the API key.

This library comes in two components: A server, where all the API keys are stored in plain text, and a client that requests the api keys from the server.
The library uses public key encryption to transmit the key from the server to the client with the enhancement that no public key may be used twice.

This approach is not entirely foolproof, because anyone can create a RSA keypair, send the public key to the server and decrypt the response with the private key, 
but this is also somewhat your aim if you create an application with anonymous access.

## Download
We don't use GitHub releases and publish on Bintray instead. You can download the server package [here](https://bintray.com/vatbub/fokprojectsReleases/safeAPIKeyStore.server#downloads).

## Usage
Create a `*.properties`-file with the api keys that you need. The file should look like the `sampleAPIKeys.properties`-file in ths repository.
Now upload this file together with the `safeAPIKeyStore.server-1.0-SNAPSHOT-jar-with-dependencies.jar` to a server. 
On the server, run `java -jar safeAPIKeyStore.server-1.0-SNAPSHOT-jar-with-dependencies.jar -apiKeyFile apiKeys.properties --port 8080`
The port option is optional. If no port is specified, it defaults to port 1650.
This will launch the server.

In your application, add the following maven dependency:

```xml
<dependency>
    <groupId>com.github.vatbub</groupId>
    <artifactId>safeAPIKeyStore.client</artifactId>
    <version>1.0</version>
</dependency>
```

Now, call `APIKeyClient.getApiKey(serverHost, serverPort, apiKeyName)` to get the api key.

# Disclaimer
Even when using this library, your API keys will be effectively publicly available. This fact may violate the Terms of service of the API provider.
The project maintainers and contributors are not liable for any legal consequences that arise by using this library. 
