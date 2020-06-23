import React from "react";
import Card from "./Card";

const CardList = ({ items }) => {

    return (
        <div>
            <div>
                {
                    items.length > 0 && items.map( (item)=> {
                        return <Card key={item.link} item={item}> {item.title}</Card>;
                    })
                }
            </div>
        </div>
    );
}

export default CardList;