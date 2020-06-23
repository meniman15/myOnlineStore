import React from "react";

const Card = (props) => {
    let item = props.item;
    return (
      <div className="tc bg-light-green br3 pa3 ma2 dib bw2 shadow-5" style={{maxWidth:"300px",maxHeight: "417.5px"}}>
          <h2 style={{overflow: "hidden", height: "3.5em"}}>{item.title}</h2>
          {item.imgUrls !==[]  &&
              <a href={item.link} target="_blank" rel="noopener noreferrer">
                  <img src={item.imgUrls[0]} className='grow shadow-5' alt="presentation" width="200" height="200"
                   onError={(event)=>
                   {event.target.setAttribute("src",'https://w7.pngwing.com/pngs/426/315/png-transparent-illustration-of-shoe-sneakers-shoe-converse-track-running-shoes-outline-white-text-hand.png')}}/>
              </a>
          }
          <div id="card-footer" style={{display:"flex",justifyContent: "space-between"}}>
              <h3>{item.price.amount} {item.price.currency}</h3>
              {item.originalPrice !== null ? <h4 style={{textDecoration: "line-through",color:"red"}}>{item.originalPrice.amount} {item.originalPrice.currency}</h4> : ""}
              <h3>{item.condition}</h3>
          </div>
      </div>
    );
};

export default Card;