import React, {Component} from "react";
import {Button, Divider, Dropdown, Input, Modal} from "semantic-ui-react";

import {withVisible} from "../components-ui/withVisible";
import {withSaving} from "../components-ui/withSaving";
import styled from "styled-components";
import {t} from "../utils/messageTexts";


const WalletGenerateContainer = styled.div`
  overflow: hidden;
  padding-bottom: 15px;
`;

class WalletGenerateComponent extends Component {
    state = {
        walletRequest: null,
        blockchain: null
    };

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
        const { walletRequest, blockchain } = this.state;
        this.setState({ isSaving: true });

        submitWithSaving({ walletRequest, blockchain });
    };

    render() {
        const { isVisible, onHide, isSaving } = this.props;

        return (
            <WalletGenerateContainer>
                <Button primary type="button" floated="right" onClick={this.onShow}>
                    Generate New Address
                </Button>
                <Modal size="tiny" open={isVisible} onClose={this.onBackgroundClick}>
                    <Modal.Header>Generate Wallet</Modal.Header>
                    <Modal.Content>
                        <div>Blockchain:</div>
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
                        <span>{t('it may take a while')}</span>
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
                            content="Generate"
                            onClick={this.onSubmit}
                        />
                    </Modal.Actions>
                </Modal>
            </WalletGenerateContainer>
        );
    }
}

export const WalletGenerate = withVisible(withSaving(WalletGenerateComponent));
