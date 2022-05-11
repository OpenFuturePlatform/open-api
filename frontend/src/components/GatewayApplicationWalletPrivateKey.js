import React, {Component} from "react";
import {Button, Dropdown, Icon, Modal} from "semantic-ui-react";
import {withVisible} from "../components-ui/withVisible";
import styled from "styled-components";
import {CopyToClipboard} from "react-copy-to-clipboard/lib/Component";

const GatewayApplicationWalletPrivateKeyContainer = styled.div`
  overflow: hidden;
  padding-bottom: 15px;
`;

class GatewayApplicationWalletPrivateKeyComponent extends Component {

  state = {
    privateKey: null,
    copied: false,
  };

  onShow = async (...args) => {
    const {onShow, onSubmit} = this.props;

    try {
      const res = await onSubmit(...args);
      this.setState({privateKey: res});
      onShow();
    } catch (e) {
      console.log(e);
    }

  };

  onBackgroundClick = e => {
    const {onHide, isSaving} = this.props;
    const target = e.target;
    if (isSaving || !target.classList.contains('modals')) {
      return;
    }
    onHide();
  };


  render() {
    const {isVisible, onHide, isSaving} = this.props;
    const {privateKey} = this.state;

    return (
      <GatewayApplicationWalletPrivateKeyContainer>
        <Icon link name="share" size="large" onClick={this.onShow}/>
        <Modal size="tiny" open={isVisible} onClose={this.onBackgroundClick}>
          <Modal.Header>Address Private Key</Modal.Header>
          <Modal.Content>
            <div className="container">
              <div className="input-group">
                <input value={this.state.privateKey}
                       onChange={({target: {value}}) => this.setState({privateKey, copied: false})} />

                <CopyToClipboard text={this.state.privateKey}
                                 onCopy={() => this.setState({copied: true})}>
                   <span id="copyButton" className="input-group-addon btn" title="Click to copy">
                    <Icon link name="copy" size="large"/>
                  </span>
                </CopyToClipboard>
                {this.state.copied ? <span style={{color: 'red'}}>Copied.</span> : null}
              </div>
            </div>
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Close
            </Button>
          </Modal.Actions>
        </Modal>
      </GatewayApplicationWalletPrivateKeyContainer>
  );
  }
  }

  export const GatewayApplicationWalletPrivateKey = withVisible(GatewayApplicationWalletPrivateKeyComponent);
