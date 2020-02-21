import React, {Component}  from 'react';
import {Link} from "react-router-dom";
import enterKey from '../images/svg/enter-key.svg';
import upload from '../images/svg/upload.svg';

class WalletLoginType extends Component{
    render(){
        return(
            <section>
                <div className="select-type">
                    <h2>Select type of login</h2>
                    <div className="links">
                        <Link to="/private-key">
                            <img src={enterKey} alt="enter-key" />
                            <h3>
                                Enter <br /> private key
                            </h3>
                        </Link>
                        <Link to="/upload">
                            <img src={upload} alt="upload" />
                            <h3>
                                Upload <br /> file
                            </h3>
                        </Link>
                    </div>
                </div>
            </section>
        );
    }
}
