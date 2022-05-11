import React, {Component} from "react";
import {Button, Divider, Dropdown, Input, Modal} from "semantic-ui-react";

import {withVisible} from "../components-ui/withVisible";
import {withSaving} from "../components-ui/withSaving";
import styled from "styled-components";
import {t} from "../utils/messageTexts";
import ScaffoldField from "../components-ui/inputs/Field";
import {Field} from "redux-form";


const WalletImportContainer = styled.div`
  overflow: hidden;
  padding-bottom: 15px;
`;

class WalletImportComponent extends Component {
  state = {
    walletRequest: null,
    blockchain: null,
    address: null
  };

  onInputChange = (e, {value}) => {
    this.setState({ address: value });
  }

  onBlockchainChange = (e, {value}) => {
    this.setState({ blockchain: value });
  }

  onShow = () => {
    const { onShow, gateway } = this.props;

    this.setState({ walletRequest: { applicationId: gateway.id, webHook: gateway.webHook} });

    onShow();
  };

  onBackgroundClick = e => {
    const { onHide, isSaving } = this.props;
    const target = e.target;
    if (isSaving || !target.classList.contains('modals')) {
      return;
    }
    onHide();
  };

  onSubmit = async () => {
    const { submitWithSaving } = this.props;
    const { walletRequest, blockchain, address } = this.state;
    this.setState({ isSaving: true });

    submitWithSaving({ walletRequest, blockchain, address });
  };

  render() {
    const { isVisible, onHide, isSaving } = this.props;

    return (
      <WalletImportContainer>
        <Button primary type="button" floated="right" onClick={this.onShow}>
          Import Address
        </Button>
        <Modal size="tiny" open={isVisible} onClose={this.onBackgroundClick}>
          <Modal.Header>Import Wallet</Modal.Header>
          <Modal.Content>
            <Input focus fluid={true} placeholder='address' onChange={ this.onInputChange }>
              <input style={{
                marginTop: '10px',
                marginBottom: '5px'
              }}/>
            </Input>
            <Dropdown
              placeholder='Select Blockchain'
              fluid
              selection
              options={[
                { key: 'ether', text: 'ETH', value: 'ETH' },
                { key: 'bitcoin', text: 'BTC', value: 'BTC' },
                { key: 'binance', text: 'BNB', value: 'BNB' }
              ]}
              onChange={this.onBlockchainChange}
            />
          </Modal.Content>
          <Modal.Actions>
            <Button negative disabled={isSaving} onClick={onHide}>
              Cancel
            </Button>
            <Button
              positive
              loading={isSaving}
              icon="checkmark"
              labelPosition="right"
              content="Import"
              onClick={this.onSubmit}
            />
          </Modal.Actions>
        </Modal>
      </WalletImportContainer>
    );
  }
}

export const WalletImport = withVisible(withSaving(WalletImportComponent));
