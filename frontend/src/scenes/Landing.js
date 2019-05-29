import React from 'react';
import { Button, Icon } from 'semantic-ui-react';

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
          <a href="https://blog.openfuture.io/" target="_blank">Blog</a>
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
              <a href="https://blog.openfuture.io/" target="_blank">Blog</a>
              <a href="/support.html">Support</a>
            </div>
            <div className="soc-links">
              <a href="https://twitter.com/OpenPlatformICO" target="_blank">
                <img src="img/landing_new/Twitter.png" alt="Twitter"/>
              </a>
              <a href="https://www.facebook.com/OpenPlatformICO/" target="_blank">
                <img src="img/landing_new/Facebook.png" alt="Facebook"/>
              </a>
              <a href="https://t.me/joinchat/FDNbh0M079p5fnfOHFEJaw" target="_blank">
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
          <p className="subtext-h1-h2">Create and edit smart contracts with different<br/>
            settings in the blockchain.</p>
          <div className="button">
            <a href="https://api.openfuture.io/oauth2/authorization/google">
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
          <a href="https://twitter.com/OpenPlatformICO" target="_blank">
            <img src="img/landing_new/Twitter.png" alt="Twitter"/>
          </a>
          <a href="https://www.facebook.com/OpenPlatformICO/" target="_blank">
            <img src="img/landing_new/Facebook.png" alt="Facebook"/>
          </a>
          <a href="https://t.me/joinchat/FDNbh0M079p5fnfOHFEJaw" target="_blank">
            <img src="img/landing_new/Telegram.png" alt="Telegram"/>
          </a>
        </div>
        </div>
        <p className="footer_text">© 2019 OPEN. All Rights Reserved</p>
      </footer>




      {/*<img src="img/svg/mob-logo.svg" alt="logo" className="mob-logo" />*/}
      {/*<section className="background" />*/}
      {/*<div className="logo">*/}
      {/*  <img id="logo" src="img/svg/logo-circle.svg" alt="logo" style={{ transform: 'rotate(1.5deg)' }} />*/}
      {/*  <img src="img/svg/logo-name.svg" alt="logo" />*/}
      {/*</div>*/}
      {/*<div className="menu-landing-page menu-main" id="burger">*/}
      {/*  <a href="/oauth2/authorization/google">*/}
      {/*    <Button color="google plus">*/}
      {/*      <Icon style={{ color: '#FFFFFF' }} name="google plus" /> Login with Google*/}
      {/*    </Button>*/}
      {/*  </a>*/}
      {/*</div>*/}
      {/*<section className="navbar">*/}
      {/*  <nav>*/}
      {/*    <div className="nav-links">*/}
      {/*      <a href="/">Home</a>*/}
      {/*      <a href="what-is-open-platform">What is Open</a>*/}
      {/*      <a href="about-us">about us</a>*/}
      {/*    </div>*/}
      {/*    <div className="soc-links">*/}
      {/*      <div>*/}
      {/*        <a*/}
      {/*          className="fs-11 lh-24"*/}
      {/*          target="_blank"*/}
      {/*          rel="noopener noreferrer"*/}
      {/*          href="http://go.openmoney.digital/fc4q"*/}
      {/*        >*/}
      {/*          Slack*/}
      {/*        </a>*/}
      {/*      </div>*/}
      {/*      <div>*/}
      {/*        <a*/}
      {/*          className="fs-11 lh-24"*/}
      {/*          target="_blank"*/}
      {/*          rel="noopener noreferrer"*/}
      {/*          href="https://twitter.com/OpenPlatformICO"*/}
      {/*        >*/}
      {/*          Twitter*/}
      {/*        </a>*/}
      {/*      </div>*/}
      {/*      <div>*/}
      {/*        <a*/}
      {/*          className="fs-11 lh-24"*/}
      {/*          target="_blank"*/}
      {/*          rel="noopener noreferrer"*/}
      {/*          href="https://www.reddit.com/r/OPENPlatform/"*/}
      {/*        >*/}
      {/*          Reddit*/}
      {/*        </a>*/}
      {/*      </div>*/}
      {/*      <div>*/}
      {/*        <a className="fs-11 lh-24" target="_blank" rel="noopener noreferrer" href="http://go.openfuture.io/xc9x">*/}
      {/*          Telegram*/}
      {/*        </a>*/}
      {/*      </div>*/}
      {/*      <div>*/}
      {/*        <a*/}
      {/*          className="fs-11 lh-24"*/}
      {/*          target="_blank"*/}
      {/*          rel="noopener noreferrer"*/}
      {/*          href="https://www.facebook.com/OpenPlatformICO/"*/}
      {/*        >*/}
      {/*          Facebook*/}
      {/*        </a>*/}
      {/*      </div>*/}
      {/*      <div>*/}
      {/*        <a*/}
      {/*          className="fs-11 lh-24"*/}
      {/*          target="_blank"*/}
      {/*          rel="noopener noreferrer"*/}
      {/*          href="http://bountycontest.openmoney.digital/2043"*/}
      {/*        >*/}
      {/*          Bounty Program*/}
      {/*        </a>*/}
      {/*      </div>*/}
      {/*    </div>*/}
      {/*  </nav>*/}
      {/*  <div className="nav-back" />*/}
      {/*</section>*/}
      {/*<section className="header-landing-page">*/}
      {/*  <div className="anchor" id="Home" />*/}
      {/*  <div className="tetragon-header lg blue" />*/}
      {/*  <div className="tetragon-header md white" />*/}
      {/*  <div className="tetragon-header sm white" />*/}
      {/*  <div className="media-coverage">*/}
      {/*    <img src="img/svg/media-coverage.svg" alt="img" className="" />*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://prkit.co/openplatform/articles">*/}
      {/*      <img src="img/svg/media-coverage.svg" alt="img" className="" />*/}
      {/*    </a>*/}
      {/*  </div>*/}

      {/*  <div className="header-content">*/}
      {/*    <h1 className="fs-64 lh-85">OPEN PLATFORM</h1>*/}
      {/*    <h2 className="fs-24 lh-32 regular">*/}
      {/*      The first blockchain payments<br />*/}
      {/*      infrastructure for applications*/}
      {/*    </h2>*/}
      {/*  </div>*/}
      {/*</section>*/}
      {/*<section className="description">*/}
      {/*  <div className="anchor" id="What-is-Open" />*/}
      {/*  <img src="img/svg/asset-header.svg" alt="img" />*/}
      {/*  <div className="description-info">*/}
      {/*    <div className="item">*/}
      {/*      <p className="fs-36 lh-48 bold color-blue title">PAYMENT INFRASTRUCTURE</p>*/}
      {/*      <p className="fs-18 lh-36">*/}
      {/*        Filling the gap of application payment infrastructure required for applications to communicate on-chain*/}
      {/*        payments with their application backend.*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*    <div className="item">*/}
      {/*      <p className="fs-36 lh-48 bold color-blue title">SOLVING ON-CHAIN PAYMENTS</p>*/}
      {/*      <p className="fs-18 lh-36">*/}
      {/*        All in one. Accept, transfer payment data, verify, authorize goods for applications, both ways on and off*/}
      {/*        blockchains all with minimal blockchain knowledge.*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*  </div>*/}
      {/*</section>*/}
      {/*<section className="end-area">*/}
      {/*  <div className="tetragon-end" />*/}
      {/*</section>*/}
      {/*<section className="structure">*/}
      {/*  <div className="card-area c1">*/}
      {/*    <div className="card">*/}
      {/*      <div className="gradient" />*/}
      {/*      <img src="img/svg/asset-1.svg" alt="img" />*/}
      {/*      <p className="fs-24 lh-32 bold color-blue title">COMPONENTS</p>*/}
      {/*      <p className="fs-14 lh-24">*/}
      {/*        Providing the missing components<br />*/}
      {/*        applications need to accept cryptocurrency<br /> and the associated payment data*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*  </div>*/}
      {/*  <div className="card-area c2">*/}
      {/*    <div className="card">*/}
      {/*      <div className="gradient" />*/}
      {/*      <img src="img/svg/asset-2.svg" alt="img" />*/}
      {/*      <p className="fs-24 lh-32 bold color-blue title">DEPLOYMENT</p>*/}
      {/*      <p className="fs-14 lh-24">*/}
      {/*        Deploy an application′s payment <br />scheme on any blockchain<br /> and incorporate any payment scheme*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*  </div>*/}
      {/*  <div className="card-area c3">*/}
      {/*    <div className="card">*/}
      {/*      <div className="gradient" />*/}
      {/*      <img src="img/svg/asset-3.svg" alt="img" />*/}
      {/*      <p className="fs-24 lh-32 bold color-blue title">MULTIPLATFORM</p>*/}
      {/*      <p className="fs-14 lh-24">*/}
      {/*        Supporting Enterprise, Gaming, SaaS,<br />*/}
      {/*        even Cryptokitties-esque applications*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*  </div>*/}
      {/*  <div className="card-area c4">*/}
      {/*    <div className="card">*/}
      {/*      <div className="gradient" />*/}
      {/*      <img src="img/svg/asset-4.svg" alt="img" />*/}
      {/*      <p className="fs-24 lh-32 bold color-blue title">BEYOND THE LIMITS*</p>*/}
      {/*      <p className="fs-14 lh-24">*/}
      {/*        Operate beyond the limits of<br /> a cryptocurrency wallet*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*  </div>*/}
      {/*</section>*/}
      {/*<section className="intuitive">*/}
      {/*  <div className="anchor" id="Intuitive" />*/}
      {/*  <div className="tetragon-intuitive white" />*/}
      {/*  <div className="tetragon-intuitive blue" />*/}
      {/*  <img className="intuitive-back" src="img/svg/intuitive-back.svg" alt="back" />*/}
      {/*  <div className="intuitive-content">*/}
      {/*    <p className="fs-36 bold color-blue title">THE FIRST "DEVELOPER WALLET"</p>*/}
      {/*    <p className="lh-36">*/}
      {/*      OPEN Takes the complexities of the blockchain and simplifies<br />*/}
      {/*      them for those looking to integrate blockchain with their applications:*/}
      {/*    </p>*/}
      {/*    <div className="item">*/}
      {/*      <p className="fs-14 lh-24">*/}
      {/*        The first bridge between blockchain and databases required for sophisticated applications.*/}
      {/*      </p>*/}
      {/*    </div>*/}
      {/*    <div className="item">*/}
      {/*      <p className="fs-14 lh-24">Reducing the amount of solidity/blockchain knowledge required.</p>*/}
      {/*    </div>*/}
      {/*    <div className="item">*/}
      {/*      <p className="fs-14 lh-24">Enabling developers to develop in any language using OPEN API.</p>*/}
      {/*    </div>*/}
      {/*  </div>*/}
      {/*</section>*/}
      {/*<section className="application">*/}
      {/*  <div className="anchor" id="Application-Architecture" />*/}
      {/*  <div className="first-text">*/}
      {/*    <p className="fs-36 lh-48 bold color-blue">HOW OPEN TECHNOLOGIES WORK</p>*/}
      {/*    <p className="fs-18 lh-36">*/}
      {/*      <strong>OPEN</strong> provides the application rails to connect all on-chain<br />*/}
      {/*      payments and the complimenting proof-of-goods into<br />*/}
      {/*      any application′s backend.*/}
      {/*    </p>*/}
      {/*    <p className="fs-18 lh-36">*/}
      {/*      Think of it is as the first developer wallet all applications require <br />*/}
      {/*      with an accompanying API to enable any developer to use <br />*/}
      {/*      <strong>OPEN</strong>*/}
      {/*      to port their application to any blockchain.*/}
      {/*    </p>*/}
      {/*  </div>*/}
      {/*  <img src="img/svg/app-architecture.svg" className="info-img" alt="img" />*/}
      {/*  <img src="img/svg/app-architecture-mob.svg" className="info-img-mob" alt="img" />*/}
      {/*  <div className="second-text">*/}
      {/*    <p className="fs-18 lh-36">*/}
      {/*      The <strong>OPEN SCAFFOLDS</strong> and <strong>OPEN STATE</strong> enable application<br />*/}
      {/*      to interoperate with on-chain components.*/}
      {/*    </p>*/}
      {/*    <p className="fs-18 lh-36">*/}
      {/*      The <strong>OPEN API</strong> enables on-chain interactions to sync with<br />*/}
      {/*      an application′s database. Record and update payments,<br />*/}
      {/*      user data, user access, etc. on and off the blockchain all at once.*/}
      {/*    </p>*/}
      {/*  </div>*/}
      {/*</section>*/}
      {/*<section className="open-token">*/}
      {/*  <div className="anchor" id="Open-Token" />*/}
      {/*  <p className="fs-36 lh-48 bold color-blue center title">OPEN TOKEN</p>*/}
      {/*  <p className="lh-36 center ">*/}
      {/*    <strong>OPEN TOKEN</strong> activates Scaffolds to allow application developers to accept payments*/}
      {/*  </p>*/}
      {/*  <div className="open-token-content">*/}
      {/*    <p className="lh-36 left96">*/}
      {/*      <strong>OPEN TOKEN</strong> utilizes a unique utility<br />*/}
      {/*      design that not only powers<br />*/}
      {/*      infrastructure, but also provides a<br />*/}
      {/*      mechanism to grow developer adoption<br />*/}
      {/*      of the <strong>OPEN PLATFORM</strong>*/}
      {/*    </p>*/}

      {/*    <p className="lh-36 right96">*/}
      {/*      By utilizing a mechanism to encourage<br />*/}
      {/*      adoption of the <strong>OPEN PLATFORM</strong>,<br />*/}
      {/*      <strong>OPEN</strong> is able to support its community<br />*/}
      {/*      of users while powering growth<br />*/}
      {/*      for applications to integrate with <strong>OPEN</strong>*/}
      {/*    </p>*/}
      {/*    <img src="img/svg/open-token.svg" alt="img" />*/}
      {/*  </div>*/}
      {/*</section>*/}
      {/*<section className="end-area end-token">*/}
      {/*  <div className="tetragon-end reverse" />*/}
      {/*</section>*/}
      {/*<section className="end-area end-token">*/}
      {/*  <div className="tetragon-end reverse" />*/}
      {/*  <img className="footer-logo" src="img/svg/footer-logo.svg" alt="img" />*/}
      {/*</section>*/}
      {/*<footer>*/}
      {/*  <div className="footer-column">*/}
      {/*    <p className="lh-24 opac25">© Copyright 2018 Open. All Rights Reserved.</p>*/}
      {/*  </div>*/}
      {/*  <div className="footer-column">*/}
      {/*    <p className="bold">Connect With Us</p>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="http://go.openmoney.digital/fc4q">*/}
      {/*      Slack*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="http://go.openfuture.io/xc9x">*/}
      {/*      Telegram*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="http://bountycontest.openmoney.digital/2043">*/}
      {/*      Bounty Program*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://twitter.com/OpenPlatformICO">*/}
      {/*      Twitter*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://www.reddit.com/r/OPENPlatform/">*/}
      {/*      Reddit*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://www.facebook.com/OpenPlatformICO/">*/}
      {/*      Facebook*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://medium.com/@theOPENPlatform">*/}
      {/*      Medium*/}
      {/*    </a>*/}
      {/*  </div>*/}
      {/*  <div className="footer-column" id="link-page">*/}
      {/*    <p className="bold">Navigation Links</p>*/}
      {/*    <a href="#Home">Home</a>*/}
      {/*    <a href="#What-is-Open">What is Open</a>*/}
      {/*    <a href="#Intuitive">Intuitive</a>*/}
      {/*    <a href="#Application-Architecture">App Architecture</a>*/}
      {/*    <a href="#Open-Token">Open Token</a>*/}
      {/*  </div>*/}
      {/*  <div className="footer-column">*/}
      {/*    <p className="bold">Useful Links</p>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://medium.com/@theOPENPlatform">*/}
      {/*      Latest News*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="http://bountycontest.openmoney.digital/2043">*/}
      {/*      Join Our Contests*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://medium.com/@theOPENPlatform">*/}
      {/*      Visit Our Latest Blog*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://prkit.co/openplatform/articles">*/}
      {/*      Press*/}
      {/*    </a>*/}
      {/*    <a target="_blank" rel="noopener noreferrer" href="https://open.crisp.help/en-us/?1515552127224">*/}
      {/*      Help Desk*/}
      {/*    </a>*/}
      {/*  </div>*/}
      {/*</footer>*/}
    </div>
  );
};

export default Landing;
