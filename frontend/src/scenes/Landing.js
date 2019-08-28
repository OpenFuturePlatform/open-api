import React from 'react';

const Landing = () => {
  return (
    <div id="landing">
      <header>
        <div className="logoNew">
          <a href="https://www.openfuture.io/"><img src="img/landing_new/logo.svg" alt="logo"/></a>
        </div>
        <nav>
          <a href="/about.html">About</a>
          <a href="/developer-portal.html">Developer Portal</a>
          <a href="/bug-bounty-program.html">Bug Bounty Program</a>
          <a href="https://blog.openfuture.io/" target="_blank" rel="noopener noreferrer">Blog</a>
          <a href="/support.html">Support</a>
        </nav>

        <div className="menu menu-main menu-unic" id="burger">
          <div className="line"></div>
          <div className="line"></div>
          <div className="line"></div>
        </div>
        <section className="navbar">
          <nav>
            <div className="nav-links">
              <a href="/">Home</a>
              <a href="/about.html">About</a>
              <a href="/developer-portal.html">Developer Portal</a>
              <a href="/bug-bounty-program.html">Bug Bounty Program</a>
              <a href="https://blog.openfuture.io/" target="_blank" rel="noopener noreferrer">Blog</a>
              <a href="/support.html">Support</a>
            </div>
            <div className="soc-links">
              <a href="https://twitter.com/OpenPlatformICO" target="_blank" rel="noopener noreferrer">
                <img src="img/landing_new/Twitter.png" alt="Twitter"/>
              </a>
              <a href="https://www.facebook.com/OpenPlatformICO/" target="_blank" rel="noopener noreferrer">
                <img src="img/landing_new/Facebook.png" alt="Facebook"/>
              </a>
              <a href="https://t.me/joinchat/FDNbh0M079p5fnfOHFEJaw" target="_blank" rel="noopener noreferrer">
                <img src="img/landing_new/Telegram.png" alt="Telegram"/>
              </a>
            </div>
          </nav>
          <div className="nav-back"></div>
        </section>
      </header>
      <section className="scaffold">
        <div className="container">
          <h1>OPEN Scaffold Generator</h1>
          <p className="subtext-h1-h2">Create and edit smart contracts with different <br/> settings in the blockchain.</p>
          <div className="button">
            <a href="/oauth2/authorization/google">
              <button type="button">
                <img src="img/landing_new/google.svg" alt="Img"/> LOGIN WITH GOOGLE
              </button>
            </a>
          </div>
        </div>
      </section>
      <section className="open_technologies">
        <div className="container">
          <h2>How OPEN Technologies Work</h2>
          <div className="optech_two_blocks">
            <div>
              <p>
                OPEN provides the application rails to connect all on-chain payments and the complimenting
                proof-of-goods into any application’s backend.
              </p>
              <p>
                Think of it is as the first developer wallet all applications require with an accompanying API to enable
                any developer to use OPEN to port their application to any blockchain.
              </p>
            </div>
            <div>
              <p>
                OPEN Scaffolds enable the application to interoperate with on-chain components.
              </p>
              <p>
                OPEN API enables on-chain interactions to sync with an application’s database. Record and update
                payments, user data, user access, etc. on and off the blockchain all at once.
              </p>
            </div>
          </div>
        </div>
      </section>

      <div className="container">
        <img className="about_illustration" src="img/landing_new/about_illustration.png" alt="img"/>
          <img className="about_illustration_mobile" src="img/landing_new/about_illustration_mobile.png" alt="img"/>
      </div>

      <footer>
        <div class="footer_left">
        <div className="logoNew">
          <a href="/"><img src="img/landing_new/logo.svg" alt="logo"/></a>
        </div>
        <div className="social_links">
          <a href="https://twitter.com/OpenPlatformICO" target="_blank" rel="noopener noreferrer">
            <img src="img/landing_new/Twitter.png" alt="Twitter"/>
          </a>
          <a href="https://www.facebook.com/OpenPlatformICO/" target="_blank" rel="noopener noreferrer">
            <img src="img/landing_new/Facebook.png" alt="Facebook"/>
          </a>
          <a href="https://t.me/joinchat/FDNbh0M079p5fnfOHFEJaw" target="_blank" rel="noopener noreferrer">
            <img src="img/landing_new/Telegram.png" alt="Telegram"/>
          </a>
        </div>
        </div>
        <p className="footer_text">© 2019 OPEN. All Rights Reserved</p>
      </footer>


    </div>
  );
};

export default Landing;
