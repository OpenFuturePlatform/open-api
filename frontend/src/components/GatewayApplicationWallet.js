import React from "react";
import {Button, Divider, Grid, Icon, Input, Modal, Segment} from "semantic-ui-react";
import {EtherscanLink} from "../components-ui/EtherscanLink";
import {connect} from "react-redux";


import {Table} from "../components-ui/Table";
import {WalletGenerate} from "./GatewayApplicationWalletGenerate";
import {generateGatewayApplicationWallet, removeGatewayApplicationWallet} from "../actions/gateways";

import {getGatewayWalletSelector} from "../selectors/getGatewayWalletsSelector";

import {GatewayApplicationWalletRemove} from "./GatewayApplicationWalletRemove";

const getColumns = (wallets, onRemove) => [
    {
        Header: 'Wallet Address',
        accessor: 'address',
        Cell: ({ value }) => <EtherscanLink>{value}</EtherscanLink>,
        sortable: false
    },
    {
        Header: 'Blockchain',
        accessor: 'blockchain',
        width: 200,
        Cell: ({ value }) => <span>{value}</span>,
        sortable: false
    },
    {
        Header: '',
        accessor: 'address',
        width: 150,
        Cell: ({ value }) => (
            <span>
                <GatewayApplicationWalletRemove onSubmit={() => onRemove(value)} />
             </span>
        ),
        sortable: false
    }
];

class GatewayApplicationWalletComponent extends React.Component {

    onRemoveWallet = address => {
        const { gateway } = this.props;
        return this.props.removeGatewayApplicationWallet(gateway.id, address);
    };

    onGenerateWallet = blockchain => {
        const { gateway } = this.props;
        this.props.generateGatewayApplicationWallet({ applicationId: gateway.id, webHook: gateway.webHook}, blockchain);
    }

    render() {
        const { wallets, gateway } = this.props;

        const columns = getColumns(wallets, this.onRemoveWallet);
        const noDataText = 'No Wallet exist'
        return (
            <div className="table-with-add">
                <WalletGenerate  gateway={gateway} onSubmit={this.onGenerateWallet} />
                <Segment attached styles={{ padding: 0 }}>
                    <Table data={wallets} columns={columns} noDataText={noDataText} />
                </Segment>
            </div>
        );
    }
}

const mapStateToProps = (state, { gateway }) => {
    const wallets = getGatewayWalletSelector(state, gateway.id);
    return { gateway, wallets };
};

export const GatewayApplicationWallet = connect(
    mapStateToProps,
    {
        generateGatewayApplicationWallet,
        removeGatewayApplicationWallet
    }
)(GatewayApplicationWalletComponent);