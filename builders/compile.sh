#!/bin/bash

OPTION=$1 # PARAMETER to compile in a fast or full way #Options: FULL, SEMIFULL, FAST
MODULE=$2 # ONLY FOR SINGLE
DOCKER_FLAG=$3 # FLAG FOR DOCKER INSTALLATION

# LOWER CASE PARAMETERS TO AVOID POSSIBLE USER ERRORS
OPTION=${OPTION,,} 
MODULE=${MODULE,,}

CURRENT_DIR=$(pwd)

if [[ $OPTION == 'single' ]] # This option require previous compilation
then
    if test -z "$MODULE" 
    then
        echo "MODULE parameter not defined. Specify the module to execute, e.g. ./compile.sh single custom"
    else
        echo "Selected SINGLE PACKAGE compilation"
        sed -e "s|?FLAGS?|-T 1C -pl $MODULE -am|g" -e "s|?OPTION?|$OPTION|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
        chmod +x ./buildlocal-$OPTION
        sudo -E PATH=$PATH ./buildlocal-$OPTION --skip-tests --no-jacoco --no-monitor --no-kafka --no-cli --no-dlb --no-tracing /opt/COMPSs
        rm ./buildlocal-$OPTION
    fi
elif [[ $OPTION == 'fast-already-done' ]] # Use this one (recommended)
then
    echo "Selected FAST compilation (30s  aprox)"
    if [[ $MODULE == 'offline' ]]
    then
    	sed -e "s|?FLAGS?|-T 1C --offline|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|true|g" -e "s|?CACHE_DEPENDENCIES?|true|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    	sed -e "s|?FLAGS?|-T 1C --offline|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    else # Use online (recommended)
    	sed -e "s|?FLAGS?|-T 3.0C -Dmaven.test.skip -DskipTests -Dmaven.javadoc.skip=true|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|true|g" -e "s|?CACHE_DEPENDENCIES?|true|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    	sed -e "s|?FLAGS?|-T 3.0C -Dmaven.test.skip -DskipTests -Dmaven.javadoc.skip=true|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    fi

    chmod +x ./buildlocal-$OPTION
    chmod +x ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    if [[ $DOCKER_FLAG  == "" ]]
    then
        sudo -E PATH=$PATH ./buildlocal-$OPTION --skip-tests --no-jacoco --no-monitor --no-kafka --no-cli --no-dlb --no-tracing --no-bindings /opt/COMPSs
    else
        ./buildlocal-$OPTION --skip-tests --no-jacoco --no-monitor --no-kafka --no-cli --no-dlb --no-tracing --no-bindings /opt/COMPSs
    fi
    rm ./buildlocal-$OPTION
    rm ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
elif [[ $OPTION == 'fast' ]]
then
    echo "Selected FAST compilation (2 minutes aprox)"
    sed -e "s|?FLAGS?|-T 1C|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|false|g" -e "s|?CACHE_DEPENDENCIES?|false|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    sed -e "s|?FLAGS?|-T 1C|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    chmod +x ./buildlocal-$OPTION
    chmod +x ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    sudo -E PATH=$PATH ./buildlocal-$OPTION --skip-tests --no-jacoco --no-monitor --no-kafka --no-cli --no-dlb --no-tracing /opt/COMPSs
    rm ./buildlocal-$OPTION
    rm ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
elif [[ $OPTION == 'semifull' ]]
then
    echo "Selected SEMIFULL compilation (5 minutes aprox)"

    if [[ $DOCKER_FLAG  == "" ]]
    then
        sudo -E PATH=$PATH ./buildlocal --skip-tests --no-jacoco --no-kafka --no-cli --no-dlb /opt/COMPSs
    else
        ./buildlocal --skip-tests --no-jacoco --no-kafka --no-cli --no-dlb /opt/COMPSs
    fi
elif [[ $OPTION == 'semifull-fast-already-done' ]]
then
    echo "Selected SEMI FULL FAST compilation"
    if [[ $MODULE == 'offline' ]]
    then
    	sed -e "s|?FLAGS?|-T 100|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|true|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    	sed -e "s|?FLAGS?|-T 100|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    else
    	sed -e "s|?FLAGS?|-T 100|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|true|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    	sed -e "s|?FLAGS?|-T 100|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    fi
    chmod +x ./buildlocal-$OPTION
    chmod +x ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    sudo -E PATH=$PATH ./buildlocal-$OPTION --skip-tests --no-bindings --no-kafka --no-cli --no-dlb --no-jacoco /opt/COMPSs
    rm ./buildlocal-$OPTION
    rm ../utils/storage/redisPSCO/make_bundle-$OPTION.sh

elif [[ $OPTION == 'full' ]]
then
    echo "Selected FULL compilation (30 minutes aprox)"
    sudo -E PATH=$PATH ./buildlocal /opt/COMPSs
elif [[ $OPTION == 'full-fast' ]] # W.I.P
then
    echo "Selected FULL-FAST compilation (? aprox)"
    sed -e "s|?FLAGS?|-T 100|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|false|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    sed -e "s|?FLAGS?|-T 100|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    chmod +x ./buildlocal-$OPTION
    chmod +x ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    sudo -E PATH=$PATH ./buildlocal-$OPTION --skip-tests /opt/COMPSs 
    rm ./buildlocal-$OPTION
    rm ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
elif [[ $OPTION == 'full-fast-already-done' ]]
then
    echo "Selected FULL FAST compilation"
    if [[ $MODULE == 'offline' ]]
    then
    	sed -e "s|?FLAGS?|-T 100|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|true|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    	sed -e "s|?FLAGS?|-T 100|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    else
    	sed -e "s|?FLAGS?|-T 100|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|true|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    	sed -e "s|?FLAGS?|-T 100|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    fi
    chmod +x ./buildlocal-$OPTION
    chmod +x ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    sudo -E PATH=$PATH ./buildlocal-$OPTION --skip-tests --no-bindings /opt/COMPSs
    rm ./buildlocal-$OPTION
    rm ../utils/storage/redisPSCO/make_bundle-$OPTION.sh

elif [[ $OPTION == 'fastest-mode' ]]
then
    # Similar to fast-already-done

    echo "Selected FASTEST-MODE"
    cd ../compss/
    POM_PATH=$(pwd)
    echo $POM_PATH
    cd $CURRENT_DIR

    sed -e "s|?FLAGS?|-T 3.0C -Dmaven.test.skip -DskipTests -Dmaven.javadoc.skip=true --quiet -am -pl compss/runtime/scheduler/custom/heuristics,compss/runtime/scheduler/custom/rtheuristicsScheduler|g" -e "s|?OPTION?|$OPTION|g" -e "s|?CACHE_BINDINGS?|true|g" -e "s|?CACHE_DEPENDENCIES?|true|g" -e "s|?FASTEST_MODE?|true|g" ./template-files/buildlocal-template > ./buildlocal-$OPTION
    sed -e "s|?FLAGS?|-T 3.0C -Dmaven.test.skip -DskipTests -Dmaven.javadoc.skip=true --quiet|g" ./template-files/make_bundle-template.sh > ../utils/storage/redisPSCO/make_bundle-$OPTION.sh

    chmod +x ./buildlocal-$OPTION
    chmod +x ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
    if [[ $DOCKER_FLAG  == "" ]]
    then
        sudo -E PATH=$PATH ./buildlocal-$OPTION --skip-tests --no-jacoco --no-monitor --no-kafka --no-cli --no-dlb --no-tracing --no-bindings /opt/COMPSs
    else
        ./buildlocal-$OPTION --skip-tests --no-jacoco --no-monitor --no-kafka --no-cli --no-dlb --no-tracing --no-bindings /opt/COMPSs
    fi
    rm ./buildlocal-$OPTION
    rm ../utils/storage/redisPSCO/make_bundle-$OPTION.sh
else
    echo "Wrong parameter for the compilation, try either 'Full' or 'Semifull' or 'Fast' or 'Fast-already-done' or 'Single' parameters"
fi
